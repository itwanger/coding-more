<template>
  <div class="outer">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>CodingMore后台管理登陆</h2>
      </div>
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item label="用户名">
          <el-input v-model="form.userLogin" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.userPass" show-password maxlength="50"></el-input>
        </el-form-item>
        <div class="text-right">
          <el-button type="primary" @click="btnLoginClick">登陆</el-button>
          <el-button @click="btnTasteClick">获取体验账号</el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="公众号二维码" :visible.sync="addTasteDialog.show" width="40%"
        :show-close="false" :center="true">
       <div style="text-align: center">
        <span class="font-title-large">扫描下方二维码关注
          <span class="color-main font-extra-large">沉默王二</span>
          公众号回复
          <span class="color-main font-extra-large">体验</span>
          获取体验账号
        </span>
        <br>
        <el-image style="width: 160px; height: 160px" :src="itwangerQrcodeUrl"></el-image>
       </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addTasteDialog.show = false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { UserLogin } from '../../api/login'
import qs from 'qs'
import { setToken } from '../../utils/auth'
export default {
  name: 'login',
  data() {
    return {
      form: {
        userLogin: 'admin',
        userPass: ''
      },
      // 添加标签对话框可见性
      addTasteDialog: {
        show: false
      },
      // 公众号二维码 URL
      itwangerQrcodeUrl: 'https://cdn.jsdelivr.net/gh/itwanger/toBeBetterJavaer/images/gongzhonghao.png'
    }
  },
  methods: {
    // 登陆按钮方法
    btnLoginClick() {
      const reqData = qs.stringify(this.form)
      UserLogin(reqData).then((res) => {
        console.log('登陆接口返回数据：', res)
        // 先保存token到cookie
        setToken(`${res.tokenHead}${res.token}`)
        // 之后跳转页面到首页
        this.$router.push('/content/articles')
      })
    },

    // 获取体验账号方法
    btnTasteClick() {
      this.addTasteDialog.show = true
    }
  }
}
</script>
<style scoped>
.box-card {
  width: 400px;
}
.box-card h2 {
  margin: 0;
  padding: 0;
}
.outer {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url(~@/assets/login-bg.jpeg) no-repeat center center;
  background-size: 100% 100%;
}
.color-main {
  color: #409eff;
}
.font-title-large {
  font-size: 18px;
  color: #303133;
}
</style>
