<template>
  <el-container>
    <!-- 左侧菜单区域 -->
    <el-aside width="201px">
      <div class="logo text-center">
        CodingMore
      </div>
      <el-menu :default-active="$route.path" class="custom-nav" router>
        <el-submenu v-for="item in pageRouters" :key="item.path" :index="item.path">
          <template slot="title">
            <i :class="item.icon"></i>
            <span>{{item.meta.title}}</span>
          </template>
          <el-menu-item v-for="subitem in item.children" :key="subitem.path" :index="item.path + '/' + subitem.path">{{subitem.meta.title}}</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <!-- 右侧布局 -->
    <el-container>
      <!-- 右侧顶部 -->
      <el-header class="flex-row-ver-center">
        <div class="bread-icon">
          <i class="el-icon-s-unfold"></i>
        </div>
        <!-- 面包屑 -->
        <div class="bread-container">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in currentMatchedRoutes" :key="item.path" :to="{ path: item.path }">{{item.meta.title}}</el-breadcrumb-item>
          </el-breadcrumb>
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
      </el-header>
      <!-- 右侧主页面部分 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { pageRouters } from '../router'
import { UserLogout } from '../api/users'
import { removeToken } from '../utils/auth'
export default {
  name: 'mainFrame',
  computed: {
    // 当前面包屑使用数据
    currentMatchedRoutes() {
      return this.$route.matched
    },
    currentUserInfo() {
      return this.$store.state.userInfo
    }
  },
  data() {
    return {
      // 在router中定义的，要显示在左侧导航的路由数组
      pageRouters
    }
  },
  methods: {
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
    }
  },
  mounted() {
    // 调用vuex的actions的refleshUserInfo方法，获取当前登陆用户信息
    this.$store.dispatch('refleshUserInfo')
  }
}
</script>

<style>
/* 面包屑前面图标容器样式 */
.bread-icon {
  margin-right: 10px;
  font-size: 18px;
  flex-shrink: 0;
}
/* 面包屑容器样式 */
.bread-container {
  flex-grow: 1;
}

/* 面包屑导航字体颜色 */
.el-header.flex-row-ver-center .el-breadcrumb__inner a,
.el-header.flex-row-ver-center .el-breadcrumb__inner.is-link,
.el-header.flex-row-ver-center .el-breadcrumb__inner,
.el-header.flex-row-ver-center .el-breadcrumb__inner:hover {
  color: #fff;
}

/* 设置菜单背景色 */
.custom-nav.el-menu {
  background-color: #030b1e;
  border-right-color: #030b1e;
}

/* 让菜单文字左对齐样式 */
.custom-nav .el-menu .el-menu-item {
  padding-left: 52px !important;
  background-color: #030b1e;
  color: #fff;
}
/* .custom-nav .el-submenu__title:hover{
  background-color: #030B1E;
} */
/* 菜单hover效果样式 */
.custom-nav .el-submenu__title:hover,
.custom-nav .el-menu .el-menu-item:hover {
  background-color: #112c6d;
}
/* 菜单激活样式 */
.custom-nav .el-menu .el-menu-item.is-active {
  background-color: #1890ff !important;
}
/* 菜单非叶子节点的样式 */
.custom-nav .el-submenu__title {
  color: #fff;
}

/* logo样式 */
.logo {
  line-height: 60px;
  font-size: 30px;
  box-sizing: border-box;
  font-style: italic;
  background-color: #d3dce6;
}

/* 这里是官网例子的样式，为了查看效果可以先拿过来使用 */
.el-header,
.el-footer {
  background-color: #030b1e;
  color: #333;
  line-height: 60px;
  color: #fff;
}

.el-aside {
  background-color: #030b1e;
  color: #030b1e;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
}
</style>
