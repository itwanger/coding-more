<template>
  <el-container>
    <!-- 左侧菜单区域 -->
    <el-aside width="200px">
      aside
    </el-aside>
    <!-- 右侧布局 -->
    <el-container>
      <!-- 右侧顶部 -->
      <el-header>
        header
      </el-header>
      <!-- 右侧主页面部分 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'mainFrame'
}
</script>

<style>
/* 这里是官网例子的样式，为了查看效果可以先拿过来使用 */
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
