# 编程喵（Codingmore）

<p align="center">
  <a href="#公众号" target="_blank"><img src="https://img.shields.io/badge/公众号-沉默王二-brightgreen.svg"></a>
  <a href="https://tobebetterjavaer.com/zhishixingqiu/" target="_blank"><img src="https://img.shields.io/badge/交流-知识星球-blue.svg"></a>
  <a href="https://tobebetterjavaer.com/" target="_blank"><img src="https://img.shields.io/badge/学习教程-Java 程序员进阶之路-red"></a>
  <a href="https://github.com/itwanger/codingmore-admin-web" target="_blank">
    <img src="https://img.shields.io/badge/前端项目-codingmore--admin--web-orange.svg" alt="前端项目">
  </a>
    <a href="https://gitee.com/itwanger/coding-more" target="_blank">
    <img src="https://img.shields.io/badge/码云-项目地址-yellow.svg" alt="前端项目">
  </a>
</p>

## 友情提示

> 1. **快速体验项目**：[在线访问地址](http://www.codingmore.top/admin/) 。
> 2. **全套学习教程**：[《Java 程序员进阶之路》](https://tobebetterjavaer.com/) 。
> 3. **专属学习路线**：学习不走弯路，整理了一套非常不错的[《Java 后端学习路线》](#公众号)。
> 5. **项目交流**：想要加群交流项目的朋友，可以加入 [知识星球，提供一对一 VIP 洗脚服务](https://tobebetterjavaer.com/zhishixingqiu/) 。

## 前言

编程喵（Codingmore）项目致力于打造一个完整的编程类学习网站，从教程到实战，一站式打法，技术上引领潮流，文档上无微不至。

## 项目文档

- 文档地址：[http://www.codingmore.top](http://www.codingmore.top)
- 源码地址：[https://github.com/itwanger/codingmore-learning](https://github.com/itwanger/codingmore-learning)

## 项目介绍

编程喵🐱（Codingmore）是一套成熟的编程类学习网站，基于 SpringBoot+Vue实现。Web 端包含首页门户、文章搜索、文章展示、文章推荐等模块。网站管理端包括统计报表、文章管理、栏目管理、权限管理等模块。

### 项目展示

#### 后台管理系统

后台管理的 Web 端 `codingmore-admin-web` 地址：[https://github.com/itwanger/codingmore-admin-web](https://github.com/itwanger/codingmore-admin-web)

后台管理系统的演示地址：[http://www.codingmore.top/admin](http://www.codingmore.top/admin)

![](http://cdn.tobebetterjavaer.com/codingmore/codingmore-admin.jpg)

#### Web 前端

Web 前端的演示地址：[http://www.codingmore.top](http://www.codingmore.top/)

网页端：

![](http://cdn.tobebetterjavaer.com/codingmore/codingmore-web.jpg)

手机端：

![](http://cdn.tobebetterjavaer.com/codingmore/codingmore-web-mobile.png)

### 组织结构：

```lua
- codingmore-admin，后台管理系统接口
- codingmore-common，工具类及通用代码
- codingmore-demo，示例代码
- codingmore-mbg， MyBatis-Plus 生成的数据库操作代码
- codingmore-security：SpringSecurity封装公用模块
- codingmore-web：前台展示系统接口
- doc：项目文档
```

### 技术选型

#### 后端技术

技术 |说明 |官网
---|---|---
Spring Boot| 容器+MVC 框架|https://spring.io/projects/spring-boot 
SpringSecurity |认证和授权框架|https://spring.io/projects/spring-security 
MyBatis |ORM 框架|http://www.mybatis.org/mybatis-3/zh/index.html
MyBatis-Plus| MyBatis 增强工具|https://baomidou.com/
Nginx |静态资源服务器|https://www.nginx.com/   
Druid |数据库连接池|https://github.com/alibaba/druid   
Lombok |简化对象封装工具|https://github.com/rzwitserloot/lombok   
Swagger-UI |文档生成工具|https://github.com/swagger-api/swagger-ui  
 Hibernator-Validator|验证框架            | http://hibernate.org/validator    
Hutool               | Java工具类库        | https://github.com/looly/hutool  
OSS                  | 对象存储            | https://github.com/aliyun/aliyun-oss-java-sdk 

#### 前端技术

技术 |说明 |官网
---|---|---
Vue |前端框架| https://vuejs.org/ 
Vue-router |路由框架| https://router.vuejs.org/ 
Vuex |全局状态管理框架| https://vuex.vuejs.org/ 
Element |前端UI框架| https://element.eleme.io 
Axios |前端HTTP框架| https://github.com/axios/axios 
Js-cookie |cookie管理工具| https://github.com/js-cookie/js-cookie 
nprogress |进度条控件| https://github.com/rstacruz/nprogress 

#### 架构图

##### 系统架构图

![](http://cdn.tobebetterjavaer.com/codingmore/codingmore.drawio.png)


##### 业务架构图

![](http://cdn.tobebetterjavaer.com/codingmore/codingmore-business.jpg)

## 环境搭建

### 开发工具

工具| 说明 |官网
---|---|---
Intellij IDEA |开发环境|https://www.jetbrains.com/idea/download    
Navicat |数据库连接工具|http://www.formysql.com/xiazai.html  
Xmind |思维导图设计工具|https://www.xmind.cn/download/

### 开发环境

工具 |版本号 |下载
---|---|---
JDK| 1.8 | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
MySQL|5.7    | https://www.mysql.com/       
Redis|5.0    | https://redis.io/download     
Nginx |1.10   | http://nginx.org/en/download.html      

### 搭建步骤

>Windows 环境部署

[如何在本地（Windows环境）跑起来编程喵（Spring Boot+Vue）项目源码？](http://www.codingmore.top/78.html)

>macOS 环境部署

[如何在本地（macOS环境）跑起来编程喵（Spring Boot+Vue）项目源码？](http://www.codingmore.top/77.html)

>云服务器/Linux环境部署

[编程喵🐱实战项目如何在云服务器上跑起来？](http://www.codingmore.top/79.html)

# 公众号

学习不走弯路，关注公众号「**沉默王二**」，回复「**学习路线**」，获取编程喵学习网站专属学习路线！

![当图片不显示的时候可以微信搜索「沉默王二」](https://itwanger-oss.oss-cn-beijing.aliyuncs.com/codingmore/banner/gongzhonghao.png)

加微信交流，公众号后台回复「**加群**」即可。

# 参与人

|  参与人 |   角色 | 公众号                            |
| ------------ | ------------ |--------------------------------|
|  沉默王二 | 产品经理  | 关注公众号「**沉默王二**」,专注 Java 后端技术分享 |
|  程序员石磊 | 后端架构  | 关注公众号「**程序员石磊**」,在职读研、云原生技术    |
|  谷明 | 前端架构  |                                |

## 许可证

[Apache License 2.0](https://github.com/itwanger/codingmore/blob/master/LICENSE)

Copyright (c) 2022 [沉默王二](https://tobebetterjavaer.com)/程序员石磊/谷明



