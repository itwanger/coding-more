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
          <el-button @click="btnResetClick">重置</el-button>
        </div>
      </el-form>
    </el-card>
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
        userLogin: '',
        userPass: ''
      }
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

    // 重置界面输入框方法
    btnResetClick() {
      this.form.userLogin = this.userPass = ''
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
</style>
