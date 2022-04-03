# codingmore后台管理前端页面开发过程全记录

## 1. 开发前准备

### 1.1 nodejs版本检查并升级到最新

从官网首页看到，目前的版本情况是这样的：

![image-20220128215201078](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128215201078.png)

LTS版，意思是长期支持的，而且看到下面写着Recommended For Most Users，就是推荐大多数用户使用。所以咱们nodejs版本选择16.13.2。然后查看本机nodejs版本：windows+R 敲cmd，打开命令行，输入命令：node -v查看当前我本机版本：

![image-20220128215402479](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128215402479.png)

然后windows升级本机版本就是重装在相同目录即可，如果不知道之前nodejs装在哪里了，可以在命令行上输入命令：where node 来查看之前的安装位置：

![image-20220128215510697](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128215510697.png)

然后将刚才从官网上下载的16.13.2双击安装到相同目录即可。

注意安装目录的选择，我之前是默认安装的，所以这个位置不用修改：

![image-20220128215643607](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128215643607.png)

一直next装完即可。然后再次在cmd中输入命令：node -v 查看安装完后的nodejs版本，如下图就是安装成功了。

![image-20220128215853057](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128215853057.png)



### 1.2 npm版本检查并升级到最新

在cmd中输入命令npm -v 查看npm版本。输入如下命令进行升级：

```
npm install -g npm
```

更新之后的状态：

![image-20220128221416625](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128221416625.png)

安装yarn，yarn之于npm的优点就不用说了，有兴趣的朋友可自行百度，安装命令：

```
npm install --global yarn
```

### 1.3 vue cli版本检查并升级到最新

在cmd中输入命令：vue -V，注意V大写，然后显示如下：

![image-20220128221800777](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220128221800777.png)

先输入命令卸载旧版本vue cli：npm uninstall @vue/cli -g

![image-20220131001304052](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220131001304052.png)

这里注意，如果本机电脑上是4以前的版本，那么卸载命令应该写vue-cli而不是@vue/cli

之后输入命令：npm install -g @vue/cli进行最新版本的安装

安装之后，再次查看vue cli版本，发现已经升级到最新版本

![image-20220131002331150](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220131002331150.png)

由于网络原因，安装有可能会失败：

![image-20220131002401323](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220131002401323.png)

这种情况不用担心，重新运行安装命令安装一下就好了。

## 2.创建项目

打开cmd，cd到项目根目录下，输入如下指令创建项目：

vue init webpack codingmore-admin-web

![image-20220131221321025](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220131221321025.png)

最后一步注意选择使用yarn

命令行运行完初始化项目之后，我们使用vscode打开项目文件夹，可以看到初始结构如下：

![image-20220131222015557](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220131222015557.png)

从vscode中，左上方菜单“终端”->“新建终端”，在终端中输入命令：

```
yarn run dev
```

测试启动，访问地址默认是8080，如下图：

![image-20220201151212717](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220201151212717.png)

## 3.引入必要组件

### 3.1 element-ui引入

我们页面上所有布局都优先使用element-ui完成。在终端或者命令行cd到项目文件夹目录下，输入命令：

```
yarn add element-ui
```

### 3.2 axios引入

由于我们要从后台获取数据，所以需要一个可靠方便的ajax提交库，axios是目前最主流的选择。在终端或者命令行cd到项目文件夹目录下，输入命令：

```
yarn add axios
```

### 3.3 vuex引入

由于有些信息，比如用户信息我们希望进行全局统一管理，所以这里我们使用vuex作为全局状态管理插件。在终端或者命令行cd到项目文件夹目录下，输入命令：

```
yarn add vuex
```



## 4.创建必要目录和文件

此时我们的项目目录是这样的：

![image-20220201165024958](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220201165024958.png)

src文件夹下面是我们主要工作的目录，当前已经有：

- assets：里面放着一张图片，所以这里我们把它当做存放静态文件的地方
- components：component意为组件，这里存放我们封装的可重用的组件
- router：路由，这里存放路由定义的信息
- App.vue：最顶层组件
- main.js：主js文件

那么下面我们为了达到一下目的，还需要创建如下目录：

- layout：将页面布局主框架相关的文件存放在这里
- views：以每个页面为单位的vue文件存放目录，可以按照项目模块再详细划分子目录
- store：使用Vuex进行全局状态管理
- api：跟后台脚本交互对接
- styles：存储通用样式文件的地方
- utils：各种通用脚本，例如common.js，request.js

## 5. 一些通用设置和封装

- 从main.js中，引入ElementUI，在头部加入以下代码：

  ```javascript
  import ElementUI from 'element-ui'
  import 'element-ui/lib/theme-chalk/index.css'
  
  Vue.use(ElementUI)
  ```

- 当创建项目的时候，main.js中已经默认引入了router，下面引入Vuex。在src/store目录下，新建index.js文件，其中插入如下代码：

  ```javascript
  import Vue from 'vue'
  import Vuex from 'vuex'
  
  Vue.use(Vuex)
  
  export default new Vuex.Store({
      state:{},
      mutations:{},
      actions:{}
  })
  ```

  并从main.js中引入Vuex，代码如下：

  ![image-20220203123950798](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203123950798.png)

- 在styles文件夹下面创建common.css，设置一些通用样式，并从main.js引入

- 在utils文件夹下创建request.js文件，用来封装axios统一跟服务器的交互格式

  在这一步当中，我们经过与后端同事的交流，接口的请求头需要传递登陆成功后统一返回的token。所以我们首先要有地方存储token，这里我们选择使用js-cookie来使用浏览器缓存的方式存储token

- 在终端输入如下命令，下载js-cookie的包：

  ```
  yarn add js-cookie
  ```

  之后再utils文件夹下创建auth.js的文件，封装存、取、删cookie的代码：

  ```javascript
  import Cookies from 'js-cookie'
  import { createUuid } from './common'
  
  // 随机存储cookie的token key
  const TokenKey = createUuid()
  
  export function getToken () {
    return Cookies.get(TokenKey)
  }
  
  export function setToken (token) {
    return Cookies.set(TokenKey, token)
  }
  
  export function removeToken () {
    return Cookies.remove(TokenKey)
  }
  ```

  当然了，common.js中添加了生成随机uuid的方法：createUuid

- 之后咱们回到request.js，首先创建请求对象：

  ```javascript
  const httpRequest = axios.create
  ```

  然后封装请求和响应的处理：

  ```javascript
  httpRequest.interceptors.request.use...
  ```

  ```javascript
  httpRequest.interceptors.response.use...
  ```

  最后再把请求统一添加加载中效果，这里利用ElementUI的Loading组件

  具体代码请看文件request.js代码

  然后这里分享一个vscode中的小技巧，使用 // #region  注释内容单独一行开始一个块注释，然后结尾再单独一行 // #endregion，就可以在vscode中折叠一个代码块，如图：

  ![image-20220203150433387](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203150433387.png)

  折叠后效果：

  ![image-20220203150450163](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203150450163.png)

  简单的封装就先做到这里，之后我们开始页面的制作。

  

## 6. 页面制作

### 6.1 目录组织

所谓的页面，主要都在src/views文件夹下面，那么咱们看通常咱们使用的views文件夹内部的目录和文件组织方式：

经过一些经验性总结和观察，我觉得目前最适合的目录组织形式是：

views

​	--模块文件夹

​		--页面文件夹

​			--index.js

​			--页面.vue文件

​			--有可能拆分的页面部分.vue文件

下面说一下为什么要这样组织：

- 首先从页面的角度来讲，我们希望按照模块去划分，这样的话逻辑也更加清晰，而且在页面较多的情况下，咱们不会打开views文件夹之后，就陷入一堆文件夹的海洋。这样有利于开发和维护时候的条理清晰。

- 之后每个页面再单独建立一个文件夹的原因有两点：
  - 一个页面有可能很复杂，可能单独的部分还需要单独拆分出vue文件，这样最后有一个页面主文件和若干组成部分文件，这样放在一个文件夹里，有利于维护的时候理清条理。
  - 一个页面有可能有不同版本，那么新版本上线又想保持随时回退的时候，把同一个路由指向的页面的新老版本放在同一个文件夹，这样路由在引用的时候只会读取index.js中export的页面，路由上保持稳定的同时，又保留了新老版本的灵活性。

基于以上原因，我们推荐views中的目录以以上方式进行组织。但是这只是推荐组织方式，并不是唯一正确或者最好的组织方式。

### 6.2 创建目录及页面文件

根据以上目录组织方式，我们创建如下目录和.vue文件：

![image-20220203154355355](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203154355355.png)

第一版的codingmore可能不包含权限管理和日志管理，所以系统管理这块，目前只涉及到用户管理

先用以下代码简单初始化各个vue文件：

![image-20220203154557571](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203154557571.png)

index.js中默认导出该view的主页面文件，例如：

![image-20220203154803474](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203154803474.png)

### 6.3 搭建基础框架页面，并接入各个页面

之后咱们来到src/layout文件夹，这里要搭建包含各个刚才咱们创建页面的框架页面，包括菜单。

咱们从elementui的官网上可以找到container布局，如图：

![image-20220203162222799](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220203162222799.png)

这里咱们选择红框里的布局，左侧放菜单，右侧顶部放置当前位置和登录人基本信息。

然后再layout文件夹下创建index.js和main-frame.vue文件，然后在main-frame.vue中开始布局框架，按照示例代码，如下：

```vue
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
        main
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

```

之后修改路由如下：

```vue
import Vue from 'vue'
import Router from 'vue-router'
import mainFrame from '@/layout'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'mainFrame',
      component: mainFrame
    }
  ]
})
```

输入yarn run dev启动项目，发现报错如下图：

![image-20220204010628451](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220204010628451.png)

发现vuex未安装相关包，那么输入如下命令解决问题：

```
yarn add vuex
```

之后再次启动项目并预览，则http://localhost:8080/页面打开如下图：

![image-20220204010910602](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220204010910602.png)

这是因为App.vue里面还存在一些样式和布局，那么修改App.vue的代码如下：

![image-20220204012041620](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220204012041620.png)

然后将styles/common.css的代码修改如下：

```css
html, body, #app, .el-container{
  height: 100%;
  overflow: auto;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  font-size: 14px;
  line-height: 1.5;
  margin: 0;
  padding: 0;
}
```

将common.css引入main.js中，再次查看页面，发现页面布局已经是我们期待的样子了：

![image-20220204012156538](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220204012156538.png)

此时框架页面已经搭建完毕了，那么我们接下来定义路由。

按照目录的规划以及模块的划分，定义路由数组如下：

```vue
const pageRouters = [
  {
    path: '/content',
    name: 'content-management',
    component: mainFrame,
    children: [
      {
        path: 'articles',
        name: 'article-management',
        component: articles
      },
      {
        path: 'columns',
        name: 'columns-management',
        component: columns
      }
    ]
  },
  {
    path: '/system',
    name: 'system-management',
    component: mainFrame,
    children: [
      {
        path: 'users',
        name: 'users-management',
        component: users
      },
      {
        path: 'power',
        name: 'power-management',
        component: power
      },
      {
        path: 'log',
        name: 'log-management',
        component: log
      }
    ]
  },
  {
    path: '/404',
    name: 'error-page404',
    component: page404
  },
  {
    path: '/500',
    name: 'error-page500',
    component: page500
  },
  {
    path: '/login',
    name: 'login',
    component: pageLogin
  }
]
```

src/layout/main-frame.vue文件中，主内容区域要使用<router-view />进行占位。如图：

![image-20220206115540346](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220206115540346.png)

之后在终端运行命令：yarn run dev

验证各个路由是否显示正常，当前咱们根据上面数组进行定义的路由有：

/content/articles

/content/columns

/system/users

/system/power

/system/log

/404

/500

/login

分别验证以上路径的页面是否显示正确。

### 6.4 登陆页面

首先先从网上找一张登陆页面的背景图：

![login-bg](D:\workspace_vs\coding-more\codingmore-admin-web\src\assets\login-bg.jpeg)

然后选择ElementUI的卡片和Form表单进行登录页面的布局：

```vue
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

```

现在需要绑定登陆按钮和重置按钮的点击事件。

- 登陆按钮逻辑：根据当前用户输入的用户名和密码，给登陆接口发送请求，看是否登录成功。如果成功，保存登陆用户信息，然后跳转到文章管理页面。如果登陆不成功，提示服务器端返回的错误信息。
- 重置按钮逻辑：清空当前用户名和密码的输入框。

使用@click事件给两个按钮分别绑定以上逻辑的方法即可。

那么登陆接口我们需要与java后端程序进行交互，所以在src/api文件夹下，创建login.js文件，里面对接所有后端接口。

那么src/api/login.js中的代码如下（利用我们之前在src/utils/request.js中封装的方法进行请求）：

```javascript
import request from '../utils/request'

// 用户提交登陆请求
export function UserLogin(data) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  })
}
```

之后再把export的方法引入login.vue进行使用：

![image-20220209201130873](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209201130873.png)

在方法中使用：

![image-20220209201159716](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209201159716.png)

之后就需要在前端项目中设置后端接口的代理了。

那么访问后端接口，我们希望有个统一的前缀，这里我们有两种做法：

1. 前缀包含具体ip地址和端口号，那么当部署环境变了，后端服务ip地址变了之后，前端程序也需要重新打包。这种做法这里不推荐。

2. 前缀只是多了一段URL而已，例如本来后端登陆接口地址是：http://localhost:9002/users/login，然后前端调用的时候实际调用的是：http://localhost:8080/api/users/login，也就是说，代码里的访问地址变成/api/users/login，在部署的时候，nginx配置文件中可以灵活配置后端服务的ip和端口，进行灵活的改变而不需要重新编译前端程序，我们推荐这种做法。下面来看具体做法：由于之前在src/utils/request.js中，已经包含代码：

   ![image-20220209205420439](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209205420439.png)

   那么下面我们只需要设置打包的环境变量VUE_APP_BASE_API的前缀为/api即可。打开config/dev.env.js，加入如下代码：

   然后打开config/index.js文件，添加如下图红框中的代码：

   ![image-20220209205830929](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209205830929.png)

   之后启动项目，查看效果。

   在登陆界面输入用户名和密码之后，发现返回了奇怪的错误：

   ![image-20220209211437208](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209211437208.png)

   前端F12控制台输出如下：

   ![image-20220209211520105](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209211520105.png)

   

   java后台控制台输出的错误信息如下：

   ![image-20220209210212248](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209210212248.png)

   说明这种情况下，java后台并没有接收到我们的传值。

   我们查看swagger文档，发现了如下信息：

   ![image-20220209210350587](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209210350587.png)

   而通过浏览器F12调试可以发现我们提交数据的请求类型是application/json的格式：

   ![image-20220209210636309](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209210636309.png)

   所以问题出在提交格式上。那么axios默认的请求类型是application/json。所以后端程序不能正常接收我们提交的数据。

   解决这个问题有两种办法：

   1. 后端同学修改一下接口的请求数据类型，改成application/json

   2. 前端同学在请求前多处理一步数据，将json数据变成form表单形式（form表单的请求类型就是application/x-www-urlencoded）的数据即可。下面说具体做法：在login.vue的script标签中引入qs工具，在提交数据之前多加一步处理数据，代码如下：

      ![image-20220209211203246](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209211203246.png)

   前面我们已经讲了具体的解决方法。那么再回头来看我们最初报错的界面：

   ![image-20220209211437208](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209211437208.png)

   这里我们封装代码的本意是想在后端程序和前端程序通信发生异常的时候，给出用户友好的提示，那么上图中这个提示明显是不够友好。那么我们来优化一下之前封装的代码：

   ![image-20220209211935076](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209211935076.png)

   之前我们是不管服务器返回什么错误信息，都直接弹出给用户。那么现在既然后端并没有返回友好的提示，或者说后端这块工作还没有做到尽善尽美。那么我们从前端直接返回一个用户看得懂的提示就好。

   经过优化之后的代码如下：

   ![image-20220209212556787](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209212556787.png)

   



### 6.5 框架页面深加工

登陆之后直接进入内容管理的文章管理页面，文章管理页面具体咱们不说。先来统一布局一下框架页。简单规划一下框架页上需要继续添加的内容如下：

![image-20220209233929359](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220209233929359.png)

用ElementUI官网上的菜单例子为模板，写出菜单基本样式。在添加了以下样式之后，菜单基本显示正常了。

借鉴官网的代码：

![image-20220210211536003](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220210211536003.png)

调整默认样式的代码：

```css
/* 设置菜单背景色 */
.custom-nav.el-menu {
  background-color: #030B1E;
  border-right-color: #030B1E;
}

/* 让菜单文字左对齐样式 */
.custom-nav .el-menu .el-menu-item {
  padding-left: 52px !important;
  background-color: #030B1E;
  color: #fff;
}
/* .custom-nav .el-submenu__title:hover{
  background-color: #030B1E;
} */
/* 菜单hover效果样式 */
.custom-nav .el-submenu__title:hover,
.custom-nav .el-menu .el-menu-item:hover{
  background-color:#112C6D;
}
/* 菜单激活样式 */
.custom-nav .el-menu .el-menu-item.is-active{
  background-color:#1890FF !important;
}
/* 菜单非叶子节点的样式 */
.custom-nav .el-submenu__title {
  color: #fff;
}
```

调整之后的左侧菜单的template代码：

```vue
<!-- 左侧菜单区域 -->
    <el-aside width="201px">
      <div class="logo text-center">
        CodingMore
      </div>
      <el-menu default-active="1-1" class="custom-nav">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>导航一</span>
          </template>
          <el-menu-item index="1-1">选项1</el-menu-item>
          <el-menu-item index="1-2">选项2</el-menu-item>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>导航2</span>
          </template>
          <el-menu-item index="2-1">选项1</el-menu-item>
          <el-menu-item index="2-2">选项2</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
```

最终查看样式效果：

![image-20220210211250292](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220210211250292.png)

导航的数据我们需要使用定义的路由数据来进行遍历，这里我们先不绑定数据，先继续关注框架页其他地方的样式。

下面是右侧头部上方需要显示的当前位置的样式了。

咱们借鉴ElementUI的面包屑代码：

![image-20220210212303975](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220210212303975.png)

现在思考下头部需要放置什么信息：一个当前位置的图标，一个面包屑导航，一个显示当前登陆的用户名。那么顶部布局思路如下：

![image-20220210232839011](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220210232839011.png)

右侧显示用户的效果用的是ElementUI的Popover+Tag标签组合，因为点击用户名字要出现修改密码和退出登陆的效果：

![image-20220210233027808](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220210233027808.png)

如果想竖排显示按钮则在Popover中给按钮都加上块级元素的父容器即可。

显示当前用户的temlpate代码如下：

```vue
		<div class="user-area">
          当前用户：
          <el-popover trigger="click">
            <el-button type="primary">修改密码</el-button>
            <el-button type="danger">退出登陆</el-button>
            <el-tag style="cursor:pointer;" slot="reference">
              王二
            </el-tag>
          </el-popover>
        </div>
```

下面需要把左侧导航、顶部面包屑、当前用户信息都变成真实数据。

之前咱们在router中已经把要显示在左侧导航的路由单独定义了数组，此时需要导入这个数组，并作为data使用，关键代码如下：

![image-20220211001738621](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220211001738621.png)

引入到框架页的代码：

![image-20220211001817394](C:\Users\GuMing\AppData\Roaming\Typora\typora-user-images\image-20220211001817394.png)

循环出导航的代码：

```vue
	  <el-menu :default-active="$route.path" class="custom-nav" router>
        <el-submenu v-for="item in pageRouters" :key="item.path" :index="item.path">
          <template slot="title">
            <i :class="item.icon"></i>
            <span>{{item.meta.title}}</span>
          </template>
          <el-menu-item v-for="subitem in item.children" :key="subitem.path" :index="item.path + '/' + subitem.path">{{subitem.meta.title}}</el-menu-item>
        </el-submenu>
      </el-menu>
```

然后面包屑导航这边：

可以使用computed，定义当前匹配的路由数组：

```vue
  computed: {
    // 当前面包屑使用数据
    currentMatchedRoutes() {
      return this.$route.matched
    }
  },
```

然后遍历面包屑的代码：

```vue
		<!-- 面包屑 -->
        <div class="bread-container">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in currentMatchedRoutes" :key="item.path" :to="{ path: item.path }">{{item.meta.title}}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
```

当前登陆的用户信息，需要调用服务器接口请求。

在src/api文件夹下创建文件users.js，用来对接用户相关后台接口。对接方式跟登陆接口相同，代码如下：

```javascript
import request from '../utils/request'

// 获取当前用户登录信息
export function GetLoginUserInfo() {
  return request({
    url: '/users/info',
    method: 'get'
  })
}

```

因为用户信息是全局的，可能其他地方也需要用到。所以需要存在vuex当中，并且获得用户信息的方法，也定义在vuex当中。

那么vuex当中又分为state、mutations、actions，其中actions作为组件调用的入口。所以src/store/index.js的代码更新为：

```javascript
import Vue from 'vue'
import Vuex from 'vuex'
import {
  GetLoginUserInfo
} from '../api/users'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 定义存储当前登陆用户信息的变量
    userInfo: null
  },
  mutations: {
    // 设置用户信息的方法
    SET_USER_INFO(state, data) {
      state.userInfo = data
    }
  },
  actions: {
    // 调用后端服务接口，获取当前用户信息并存入vuex
    refleshUserInfo({commit}, callback) {
      GetLoginUserInfo().then(res => {
        console.log('获取登录用户信息成功', res)
        commit('SET_USER_INFO', res)
      })
    }
  }
})

```

最后，给框架页的退出登陆按钮对接后端退出接口，然后跳转回登录页面（此过程略）。

### 6.6 栏目管理页面



### 6.7 文章管理页面

### 6.8 文章编辑页面



































