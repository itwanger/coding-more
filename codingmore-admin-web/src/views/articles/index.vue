<template>
  <div v-loading="viewLoading" class="app-container">
    <el-row type="flex" :gutter="20">
      <!--左侧部分-->
      <el-col :span="6">
        <div class="filter-container">
          <div>
            <el-input v-model="searchTreeText" :placeholder="$t('columns.columnName')" @keyup.enter.native="searchTree">
              <el-button slot="append" icon="el-icon-search" @click="searchTree" />
            </el-input>
          </div>
        </div>
        <div class="tree-area">
          <el-tree ref="columnTree" :filter-node-method="filterNode" :auto-expand-parent="true" :highlight-current="true" :data="treeData" node-key="termTaxonomyId" :expand-on-click-node="false" :props="treeDefaultProps" @current-change="handleCurrentChange" />
        </div>
      </el-col>
      <!--右侧部分-->
      <el-col :span="18">
        <div class="filter-container">
          <div class="text-right">
            <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
              {{ $t('operation.add') }}
            </el-button>
            <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="handleCreate">
              {{ $t('operation.delete') }}
            </el-button>
          </div>
        </div>
        <el-table ref="multipleTable" :key="tableAbout.tableKey" :data="tableAbout.tableData" border fit highlight-current-row style="width: 100%;" @selection-change="handleSelectionChange">
          <el-table-column align="center" class-name="recorrect-center" type="selection" width="55px" />
          <el-table-column :label="$t('articles.id')" prop="id" width="80px" align="center" />
          <el-table-column :label="$t('articles.title')" prop="postTitle" />
          <el-table-column :label="$t('articles.summary')" prop="postExcerpt" width="200px" show-overflow-tooltip />
          <el-table-column :label="$t('articles.pubTime')" prop="postDate" width="155px" />
          <el-table-column :label="$t('articles.state')" prop="postStatus" width="80px" :formatter="statusFilter" />
          <el-table-column :label="$t('articles.sortNum')" prop="menuOrder" width="80px" align="center" />
          <el-table-column :label="$t('operation.actions')" align="center" width="180px" class-name="small-padding fixed-width">
            <template slot-scope="{row,$index}">
              <el-button type="primary" size="mini" @click="handleUpdate(row)">
                {{ $t('operation.edit') }}
              </el-button>
              <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
                {{ $t('operation.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="tableAbout.listQuery.total>0" :total="tableAbout.listQuery.total" :page.sync="tableAbout.listQuery.page" :limit.sync="tableAbout.listQuery.pageSize" @pagination="getList" />
      </el-col>
    </el-row>
    <el-dialog v-loading="dialogLoading" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="editDataModel" label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
        <!-- 标题 -->
        <el-form-item :label="$t('articles.title')" prop="postTitle">
          <el-input v-model="editDataModel.postTitle" maxlength="100" placeholder="请输入标题" />
        </el-form-item>
        <el-row>
          <el-col :span="16">
            <!-- 发布时间 -->
            <el-form-item :label="$t('articles.pubTime')" prop="postDate">
              <el-date-picker v-model="editDataModel.postDate" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择发布时间" />
              <!-- <span>**不填写为保存时间**</span> -->
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- 排序号 -->
            <el-form-item :label="$t('articles.sortNum')" prop="menuOrder">
              <el-input v-model="editDataModel.menuOrder" maxlength="10" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 摘要 -->
        <el-form-item :label="$t('articles.summary')">
          <el-input v-model="editDataModel.postExcerpt" :autosize="{ minRows: 4, maxRows: 6}" type="textarea" placeholder="请输入摘要" maxlength="1000" />
        </el-form-item>
        <!-- 文章类型 -->
        <el-row>
          <el-col :span="16">
            <el-form-item :label="$t('articles.type')" prop="postType">
              <el-select v-model="editDataModel.postType">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 正文 -->
        <el-form-item :label="$t('articles.content')" prop="postContent">
          <Tinymce ref="editor" v-model="editDataModel.postContent" :height="400" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('operation.cancel') }}
        </el-button>
        <el-button type="primary" @click="saveData('DRAFT')">
          {{ $t('operation.saveDraft') }}
        </el-button>
        <el-button type="primary" @click="saveData('PUBLISHED')">
          {{ $t('operation.save') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getArticlePagedList, getArticleById, deleteArticle, createArticle, updateArticle } from '@/api/articles'
import { getAllColumns } from '@/api/column'
import waves from '@/directive/waves' // waves directive
import Tinymce from '@/components/Tinymce'
import Pagination from '@/components/Pagination'
import { emptyChecker } from '@/utils/validate'
import { loopExpendTree } from '@/utils/common'
import qs from 'qs'
export default {
  name: 'ArticlesManagement',
  directives: { waves },
  components: { Pagination, Tinymce },
  filters: {
    typeFilter(type) {
      let typeText = ''
      const filterArr = this.typeList.filter(item => item.value === type)
      if (filterArr) {
        typeText = filterArr[0].label
      }
      return typeText
    }
  },
  data() {
    return {
      statusList: [{ value: 'PUBLISHED', label: '发布' }, { value: 'DELETED', label: '删除' }, { value: 'DRAFT', label: '草稿' }],
      typeList: [{ value: 'POST', label: '文章' }, { value: 'PAGE', label: '页面' }],

      // 文章列表相关属性
      tableAbout: {
        listQuery: {
          page: 1,
          pageSize: 15,
          total: 0,
          siteId: -1, // this.currentSite.siteId,
          termTaxonomyId: ''
        },
        tableKey: 0,
        tableData: []
      },
      viewLoading: true,
      dialogLoading: false,

      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,

      // 拼接的栏目树的根节点，也就是当前站点
      siteNodeOnTree: null,

      // 页面用来编辑的数据模型
      editDataModel: {
        id: undefined,
        postTitle: '', // 标题
        postDate: '', // 发布时间
        postContent: '', // 正文
        postExcerpt: '', // 摘要
        menuOrder: '', // 排序号
        postType: 'POST', // 文章类型
        postStatus: 'PUBLISHED', // 文章状态
        termTaxonomyId: '', // 所属栏目id
        siteId: -1 // this.currentSite.siteId
      },

      // 是否显示编辑弹窗
      dialogFormVisible: false,
      // 编辑弹窗当前状态
      dialogStatus: '',
      // 编辑弹窗显示标题字典
      textMap: {
        update: this.$t('articles.editArticle'),
        create: this.$t('articles.createArticle')
      },
      // 编辑弹窗校验规则
      rules: {
        postTitle: [{ required: true, validator: emptyChecker, message: this.$t('articles.validNotEmptyArticleTitle'), trigger: 'blur' }],
        postContent: [{ required: true, validator: emptyChecker, message: this.$t('articles.validNotEmptyArticleContent'), trigger: 'blur' }]
      },

      // 树形栏目数据
      treeData: null,
      // 当前选中树节点的数据
      treeSelectedNode: null,
      // 当前树节点过滤条件
      searchTreeText: '',
      treeDefaultProps: {
        children: 'children',
        label: 'name'
      },

      // 概览模板列表
      avaliableOutlineTemplateList: [],
      // 细览模板列表
      avaliableDetileTemplateList: []
    }
  },
  computed: {
    currentSite() {
      return this.$store.getters.siteManagement.currentSite
    }
  },
  mounted() {
    console.log('mounted中，当前站点变量：', this.currentSite)
    this.tableAbout.listQuery.siteId = this.currentSite.siteId
    this.editDataModel.siteId = this.currentSite.siteId
    // 初始化站点节点
    this.siteNodeOnTree = this.getTreeNodeOfSite()
    this.getTreeData()
  },
  methods: {
    statusFilter(row) {
      let statusText = ''
      const filterArr = this.statusList.filter(item => item.value === row.postStatus)
      if (filterArr.length > 0) {
        statusText = filterArr[0].label
      }
      return statusText
    },
    // 文章列表查询方法
    getList() {
      this.tableAbout.listQuery.termTaxonomyId = this.treeSelectedNode.termTaxonomyId === 0 ? '' : this.treeSelectedNode.termTaxonomyId
      this.viewLoading = true
      getArticlePagedList(this.tableAbout.listQuery).then(response => {
        this.tableAbout.tableData = response.result.items
        this.tableAbout.listQuery.total = response.result.total
        this.viewLoading = false
      })
    },

    // 处理当前选中节点改变方法
    handleCurrentChange(data, node) {
      if (this.treeSelectedNode !== data) {
        this.treeSelectedNode = data
        this.getList()
      }
      if (node.expanded === false) {
        node.expanded = true
      }
    },

    // 获得栏目树数据
    getTreeData() {
      this.viewLoading = true
      getAllColumns({ siteId: this.currentSite.siteId }).then(response => {
        const columns = response.result
        this.siteNodeOnTree.children = columns
        this.treeData = [this.siteNodeOnTree]
        if (this.treeSelectedNode === null) {
          this.treeSelectedNode = this.siteNodeOnTree
        }
        this.viewLoading = false
        this.$nextTick(() => {
          this.$refs.columnTree.setCurrentKey(this.treeSelectedNode.termTaxonomyId)
          const theNode = this.$refs.columnTree.getNode(this.treeSelectedNode.termTaxonomyId)
          this.handleCurrentChange(theNode.data, theNode)
          this.getList()
          console.log('获得当前选中节点：：：', theNode)
          console.log('当前tableKey', this.tableKey)
          console.log('当前treeSelectedNode', this.treeSelectedNode, theNode.data)
          loopExpendTree(this.$refs.columnTree, theNode, -1)
          if (this.searchTreeText !== '') {
            this.searchTree()
          }
        })
      })
    },
    // 触发搜索树方法
    searchTree() {
      this.$refs.columnTree.filter(this.searchTreeText)
    },
    // 按名称搜索树节点方法
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    // 处理table选中行改变方法
    handleSelectionChange(val) {
      console.log('table选中行改变', val)
    },
    // 重置栏目编辑对象
    resetSiteDataModel(currentColumnId) {
      this.editDataModel = {
        id: undefined,
        menuOrder: '', // 排序号
        postTitle: '', // 标题
        postDate: '', // 发布时间
        postContent: '', // 正文
        postExcerpt: '', // 摘要
        postType: 'POST', // 文章类型
        postStatus: 'PUBLISHED', // 文章状态
        termTaxonomyId: currentColumnId, // 所属栏目id
        siteId: this.currentSite.siteId
      }
    },
    // 获得站点树节点——即根节点
    getTreeNodeOfSite() {
      return {
        termTaxonomyId: 0,
        name: this.currentSite.siteName,
        tplPath: this.currentSite.telName,
        tplDetailPath: '',
        description: '',
        parentId: -1,
        siteId: this.currentSite.siteId,
        children: null
      }
    },
    // 新增按钮点击方法
    handleCreate() {
      if (this.treeSelectedNode.termTaxonomyId === 0) {
        this.$alert('请选择要创建文章的栏目', {
          type: 'error'
        }).catch(() => { })
        return false
      }
      if (this.$refs.editor) {
        this.$refs.editor.setContent('')
      }
      this.resetSiteDataModel(this.$refs.columnTree.getCurrentKey())
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 修改按钮点击方法
    handleUpdate(row) {
      this.viewLoading = true
      getArticleById({ postsId: row.id }).then(response => {
        this.editDataModel = response.result
        this.editDataModel.termTaxonomyId = row.termTaxonomyId
        this.dialogStatus = 'update'
        this.viewLoading = false
        this.dialogFormVisible = true
        this.$nextTick(() => {
          if (this.$refs.editor) {
            this.$refs.editor.setContent(this.editDataModel.postContent)
          }
          this.$refs['dataForm'].clearValidate()
        })
      })
    },
    // 保存文章方法
    saveData(stateSetting) {
      this.editDataModel.postStatus = stateSetting
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.dialogLoading = true
          const postData = qs.stringify(this.editDataModel)

          const saveFunc = this.dialogStatus === 'create' ? createArticle : updateArticle

          saveFunc(postData).then(() => {
            this.dialogFormVisible = false
            this.dialogLoading = false
            this.getList()
            this.$notify({
              title: this.$t('operation.success'),
              message: this.$t('operation.operationSuccessed'),
              type: 'success',
              duration: 2000
            })
          }).catch(() => {
            this.dialogFormVisible = false
            this.dialogLoading = false
          })
        }
      })
    },
    // 行删除按钮处理
    handleDelete(row, index) {
      const that = this
      const confirmMes = that.$t('articles.deleteWarning')
      this.$confirm(confirmMes, that.$t('operation.systemInfo'), {
        confirmButtonText: that.$t('operation.confirm'),
        cancelButtonText: that.$t('operation.cancel'),
        type: 'warning'
      }).then(() => {
        this.viewLoading = true
        deleteArticle({ postsId: row.id }).then(() => {
          this.$notify({
            title: this.$t('operation.success'),
            message: this.$t('operation.deleteSuccessed'),
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      })
    }
  }
}
</script>
<style scoped>
.left-part {
  width: 35%;
  padding-right: 10px;
}
.right-part {
  width: 65%;
  padding-left: 10px;
}
.tree-area {
  margin-top: 10px;
}
</style>
