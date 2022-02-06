<template>
  <div class="outer">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <h2>CodingMore后台管理登陆</h2>
      </div>
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item label="用户名">
          <el-input v-model="form.loginName"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.loginPwd" show-password></el-input>
        </el-form-item>
        <div class="text-right">
          <el-button type="primary">登陆</el-button>
          <el-button>重置</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'login',
  data () {
    return {
      form: {
        loginName: '',
        loginPwd: ''
      }
    }
  },
  methods: {

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
