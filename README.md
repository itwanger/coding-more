# 编程喵（Codingmore）

<p align="center">
  <a href="#公众号" target="_blank"><img src="https://img.shields.io/badge/公众号-沉默王二-brightgreen.svg"></a>
  <a href="#公众号" target="_blank"><img src="https://img.shields.io/badge/交流-微信群-blue.svg"></a>
  <a href="https://github.com/itwanger/codingmore-learning" target="_blank"><img src="https://img.shields.io/badge/学习教程-codingmore--learning-red"></a>
  <a href="https://github.com/itwanger/codingmore-admin-web" target="_blank">
    <img src="https://img.shields.io/badge/前端项目-codingmore--admin--web-orange.svg" alt="前端项目">
  </a>
    <a href="https://gitee.com/itwanger/coding-more" target="_blank">
    <img src="https://img.shields.io/badge/码云-项目地址-yellow.svg" alt="前端项目">
  </a>
</p>

## 友情提示

> 1. **快速体验项目**：[在线访问地址](http://www.codingmore.top/admin/index.html)。
> 2. **全套学习教程**：[《codingmore学习教程》](http://www.codingmore.top/learning/README)。
> 3. **专属学习路线**：学习不走弯路，整理了套非常不错的[《codingmore专属学习路线》](#公众号)。
> 5. **项目交流**：想要加群交流项目的朋友，可以加入[codingmore项目交流群](#公众号)。

## 前言

编程喵（Codingmore）项目致力于打造一个完整的编程类学习网站，从教程到实战，一站式打法，技术上引领潮流，文档上无微不至。

## 项目文档

- 文档地址：[doc.codingmore.com](http://www.codingmore.top/learning/)
- 备用地址：[https://github.com/itwanger/codingmore-learning](https://github.com/itwanger/codingmore-learning)

## 项目介绍

编程喵（Codingmore）是一套成熟的编程类学习网站，基于 SpringBoot+MyBatis实现，采用Docker容器化部署。网站前台包含首页门户、文章搜索、文章展示、文章推荐等模块。网站管理端包括统计报表、文章管理等模块。

### 项目展示

#### 后台管理系统

后台管理系统的前端项目 `codingmore-admin-web` 地址：[https://github.com/itwanger/codingmore-admin-web](https://github.com/itwanger/codingmore-admin-web)

后台管理系统的演示地址：[http://www.codingmore.top/admin/index.html](http://www.codingmore.top/admin/index.html)

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



##### 业务架构图

#### 模块介绍

##### 后台管理系统 `codingmore-admin`

文章管理

##### 前台网站系统 `codingmore-web`

#### 开发进度

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


>Docker 环境部署

>相关环境部署

# 公众号

学习不走弯路，关注公众号「**沉默王二**」，回复「**学习路线**」，获取编程喵学习网站专属学习路线！

![当图片不显示的时候可以微信搜索「沉默王二」](https://itwanger-oss.oss-cn-beijing.aliyuncs.com/codingmore/banner/gongzhonghao.png)

加微信交流，公众号后台回复「**加群**」即可。

## 许可证

[Apache License 2.0](https://github.com/itwanger/codingmore/blob/master/LICENSE)

Copyright (c) 2022 沉默王二/程序员石磊/谷明



