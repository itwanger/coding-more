<template>
  <el-row style="margin-top: 75px;">
    <el-col :span="2">
      &nbsp;
    </el-col>
    <el-col :span="20">
      <el-form ref="dataForm" :rules="rules" :model="editDataModel" label-position="right" label-width="100px">
        <!-- 标题 -->
        <el-form-item label="文章标题" prop="postTitle">
          <el-input v-model="editDataModel.postTitle" maxlength="100" placeholder="请输入标题" />
        </el-form-item>
        <el-row>
          <el-col :span="6">
            <el-form-item label="标题显示">
              <el-radio v-model="titleDisplaySettings.textColor" label="green">绿色</el-radio>
              <el-radio v-model="titleDisplaySettings.textColor" label="blue">蓝色</el-radio>
              <el-checkbox v-model="titleDisplaySettings.isBold">粗体</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="标题右侧按钮">
              <el-switch v-model="titleDisplaySettings.rightButton.show"></el-switch>
              <el-radio-group v-show="titleDisplaySettings.rightButton.show" style="margin-left:30px;" v-model="titleDisplaySettings.rightButton.bgColor">
                <el-radio :label="''">黑色背景</el-radio>
                <el-radio :label="'green'">绿色背景</el-radio>
                <el-radio :label="'blue'">蓝色背景</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col v-show="titleDisplaySettings.rightButton.show" :span="4">
            <el-form-item label="按钮文本">
              <el-input v-model="titleDisplaySettings.rightButton.textContent" maxlength="10" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
          <el-col v-show="titleDisplaySettings.rightButton.show" :span="6">
            <el-input placeholder="请输入内容" v-model="titleDisplaySettings.rightButton.linkTo" :disabled="titleDisplaySettings.rightButton.linkType == 0" class="input-with-select">
              <el-select v-model="titleDisplaySettings.rightButton.linkType" style="width:120px;" slot="prepend" :disabled="false" placeholder="请选择">
                <el-option label="链接本文" value="0"></el-option>
                <el-option label="外链" value="1"></el-option>
              </el-select>
            </el-input>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <!-- 发布时间 -->
            <el-form-item label="发布时间" prop="postDate">
              <el-date-picker v-model="editDataModel.postDate" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择发布时间" />
              <!-- <span>**不填写为保存时间**</span> -->
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <!-- 排序号 -->
            <el-form-item label="排序号" prop="menuOrder">
              <el-input v-model="editDataModel.menuOrder" placeholder="请输入排序号" maxlength="10" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <!-- 文章标签 -->
            <el-form-item label="标签" prop="tags">
              <el-input v-model="editDataModel.tags" maxlength="100" placeholder="请输入标签，多个标签之间使用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 摘要 -->
        <el-form-item label="摘要">
          <el-input v-model="editDataModel.postExcerpt" :autosize="{ minRows: 4, maxRows: 6}" type="textarea" placeholder="请输入摘要" maxlength="1000" />
        </el-form-item>
        <!-- 正文 -->
        <el-form-item label="正文" prop="postContent">
          <mavon-editor v-model="editDataModel.postContent" ref="md" style="min-height: 800px" />
        </el-form-item>
      </el-form>
      <div class="top-bar flex-row-ver-center">
        <div class="logo-title">
          CodingMore - 编辑文章
        </div>
        <div>
          <el-button v-if="editMode === 'm'" type="danger" @click="handleDelete">
            删除
          </el-button>
          <el-button @click="saveData('DRAFT')">
            保存草稿
          </el-button>
          <el-button @click="saveData('PUBLISHED')">
            发布
          </el-button>
        </div>
        <div class="user-area">
          当前用户：
          <el-popover trigger="click">
            <el-button type="primary">修改密码</el-button>
            <el-button type="danger" @click="logoutSystemClick">退出登陆</el-button>
            <el-tag style="cursor:pointer;" slot="reference">
              {{ currentUserInfo ? currentUserInfo.username: '获取失败' }}
            </el-tag>
          </el-popover>
        </div>
      </div>
    </el-col>
    <el-col :span="2">
      &nbsp;
    </el-col>
  </el-row>
</template>
<script>
import { UserLogout } from '@/api/users'
import { removeToken } from '@/utils/auth'
import { getArticleById, deleteArticle, createArticle, updateArticle } from '@/api/articles'
import { emptyChecker } from '@/utils/validate'
import qs from 'qs'
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import Vue from 'vue'
Vue.use(mavonEditor)

export default {
  name: 'articleEdit',
  components: { mavonEditor },
  computed: {
    currentUserInfo() {
      return this.$store.state.userInfo
    }
  },
  data() {
    return {
      // 页面用来编辑的数据模型
      editDataModel: {
        postsId: undefined,
        postTitle: '', // 标题
        postDate: '', // 发布时间
        postContent: '', // 正文
        postExcerpt: '', // 摘要
        menuOrder: '', // 排序号
        postType: 'POST', // 文章类型
        postStatus: 'DRAFT', // 文章状态
        termTaxonomyId: '', // 所属栏目id
        attribute: '', // 属性
        tags: '' // 标签
      },

      // 编辑弹窗校验规则
      rules: {
        postTitle: [{ required: true, validator: emptyChecker, message: '文章标题不能为空', trigger: 'blur' }],
        postContent: [{ required: true, validator: emptyChecker, message: '文章正文不能为空', trigger: 'blur' }]
      },

      // 文章标题相关设置数据
      titleDisplaySettings: {
        isBold: false, // 是否粗体
        textColor: 'green', // 链接文字的颜色，目前支持蓝色和绿色
        rightButton: { // 右侧按钮设置
          show: false, // 是否显示右侧按钮
          bgColor: '', // 按钮背景色：绿色(green)、蓝色(blue)、黑色(默认，没有值就是黑色)
          textContent: '查看更多', // 按钮上的文字
          linkType: '0', // 链接类型
          linkTo: '' // 按钮链接是否链接本文章或者可以设置任意外链，如果不填写，就链接本篇文章
        }
      },

      // 当前编辑页面的id
      editId: null,
      // 新增时候的所属栏目id
      columnId: null,
      // 编辑模式
      editMode: null
    }
  },
  methods: {
    // 退出登陆方法
    logoutSystemClick() {
      // 调用服务器方法退出登陆
      UserLogout().then(() => {
        // 移除token
        removeToken()
        // 移除vuex中的用户信息
        this.$store.dispatch('removeUserInfo')
        // 跳转到登录页面
        this.$router.push('/login')
      })
    },
    // 弹出警告错误信息提示，点击确定后关闭窗口方法
    alertMessageAndCloseWindow(message, iconType) {
      this.$alert(message, {
        type: iconType || 'failed',
        callback: action => {
          window.close()
        }
      }).then(() => {
        window.close()
      })
    },
    // 保存文章方法
    saveData(stateSetting) {
      this.editDataModel.postStatus = stateSetting
      this.editDataModel.attribute = JSON.stringify(this.titleDisplaySettings)
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.dialogLoading = true
          const postData = qs.stringify(this.editDataModel)

          const saveFunc = this.editMode === 'n' ? createArticle : updateArticle

          saveFunc(postData).then(() => {
            this.refleshMainPageTable()
            this.alertMessageAndCloseWindow('保存成功，将关闭窗口', 'success')
          })
        }
      })
    },
    // 加载编辑的数据
    loadData() {
      getArticleById({ postsId: this.editId }).then(res => {
        this.editDataModel = res
        if (res.attribute) {
          this.titleDisplaySettings = JSON.parse(res.attribute)
        }
        this.$refs['dataForm'].clearValidate()
      }).catch(() => {
        this.alertMessageAndCloseWindow('查询编辑文章信息发生异常，请刷新文章列表重试，将关闭编辑窗口')
      })
    },
    // 刷新主页面列表
    refleshMainPageTable() {
      window.opener.refleshTable(this.columnId || this.editDataModel.termTaxonomyId)
    },
    // 文章删除方法
    handleDelete() {
      const confirmMes = '是否确认删除该文章？'
      this.$confirm(confirmMes, '系统提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.viewLoading = true
        deleteArticle({ postsId: this.editId }).then(() => {
          this.refleshMainPageTable()
          this.alertMessageAndCloseWindow('删除成功', 'success')
        }).catch((msg) => {
          this.$alert(`删除失败，服务器返回信息：${msg}`, { type: 'failed' })
        })
      })
    }
  },
  mounted() {
    this.$store.dispatch('refleshUserInfo')
    // 编辑的情况
    if (this.$route.query.aid) {
      this.editId = this.$route.query.aid
      this.editMode = 'm'
      this.loadData()
    } else if (this.$route.query.cid) {
      // 新增的情况
      this.editMode = 'n'
      this.columnId = this.$route.query.cid
      this.editDataModel.termTaxonomyId = this.columnId
    } else {
      // 参数错误的情况
      this.alertMessageAndCloseWindow('参数错误，将关闭编辑窗口')
    }
  }
}
</script>
<style scoped>
.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  line-height: 60px;
  display: flex;
  box-sizing: border-box;
  padding: 0 15px;
  background-color: #030b1e;
  color: #fff;
}
.logo-title {
  font-size: 20px;
  flex-grow: 1;
}
.user-area {
  margin-left: 15px;
}
</style>
