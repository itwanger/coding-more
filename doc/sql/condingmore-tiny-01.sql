/*
 Navicat MySQL Data Transfer

 Source Server         : codingmoretiny01
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 118.190.99.232
 Source Database       : codingmoretiny01

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : utf-8

 Date: 01/28/2022 12:56:02 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `comments`
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `comment_id` bigint NOT NULL COMMENT 'comment_ID',
  `comment_post_id` bigint DEFAULT NULL COMMENT '对应文章ID',
  `comment_author` tinytext COLLATE utf8mb4_general_ci COMMENT '评论者',
  `comment_author_email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论者邮箱',
  `comment_author_url` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论者网址',
  `comment_author_ip` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论者IP',
  `comment_date` datetime DEFAULT NULL COMMENT '评论时间',
  `comment_content` text COLLATE utf8mb4_general_ci COMMENT '评论正文',
  `comment_approved` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论是否被批准',
  `comment_agent` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论者的USER AGENT',
  `comment_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论类型(pingback/普通)',
  `comment_parent` bigint DEFAULT NULL COMMENT '父评论ID',
  `user_id` bigint DEFAULT NULL COMMENT '评论者用户ID（不一定存在）',
  PRIMARY KEY (`comment_id`),
  KEY `Key_1` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='评论表;评论表';

-- ----------------------------
--  Table structure for `links`
-- ----------------------------
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links` (
  `link_id` bigint NOT NULL COMMENT 'link_id',
  `link_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接URL',
  `link_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接标题',
  `link_image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接图片',
  `link_target` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接打开方式',
  `link_description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接描述',
  `link_visible` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否可见（Y/N）',
  `link_owner` bigint DEFAULT NULL COMMENT '添加者用户ID',
  PRIMARY KEY (`link_id`),
  KEY `Key_1` (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='链接信息表;链接信息表';

-- ----------------------------
--  Table structure for `post_tag`
-- ----------------------------
DROP TABLE IF EXISTS `post_tag`;
CREATE TABLE `post_tag` (
  `post_id` bigint NOT NULL COMMENT 'ID',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '标签名称',
  PRIMARY KEY (`post_id`),
  KEY `Key_1` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='标签表;标签表';

-- ----------------------------
--  Table structure for `post_tag_relation`
-- ----------------------------
DROP TABLE IF EXISTS `post_tag_relation`;
CREATE TABLE `post_tag_relation` (
  `post_tag_relation_id` bigint NOT NULL COMMENT '对应文章ID',
  `post_tag_id` bigint NOT NULL COMMENT '标签ID',
  `term_order` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`post_tag_relation_id`,`post_tag_id`),
  KEY `Key_1` (`post_tag_relation_id`,`post_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='标签文章关系表;标签文章关系表';

-- ----------------------------
--  Table structure for `posts`
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `posts_id` bigint NOT NULL COMMENT 'posts_id',
  `post_author` bigint DEFAULT NULL COMMENT '对应作者ID',
  `post_date` datetime DEFAULT NULL COMMENT '发布时间',
  `post_content` longtext COLLATE utf8mb4_general_ci COMMENT '正文',
  `post_title` text COLLATE utf8mb4_general_ci COMMENT '标题',
  `post_excerpt` text COLLATE utf8mb4_general_ci COMMENT '摘录',
  `post_status` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章状态',
  `comment_status` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论状态',
  `post_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `menu_order` int DEFAULT NULL COMMENT '排序ID',
  `post_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章类型（post/page等）',
  `comment_count` bigint DEFAULT NULL COMMENT '评论总数',
  `attribute` json DEFAULT NULL COMMENT '属性',
  PRIMARY KEY (`posts_id`),
  KEY `Key_1` (`posts_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章;文章';

-- ----------------------------
--  Table structure for `site`
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `site_id` bigint NOT NULL COMMENT 'site_id',
  `title` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `keywords` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关键字',
  `description` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '介绍',
  `attribute` json DEFAULT NULL COMMENT '属性',
  PRIMARY KEY (`site_id`),
  KEY `Key_1` (`site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='站点配置;站点配置';

-- ----------------------------
--  Table structure for `term_relationships`
-- ----------------------------
DROP TABLE IF EXISTS `term_relationships`;
CREATE TABLE `term_relationships` (
  `term_relationships_id` bigint NOT NULL COMMENT '对应文章ID/链接ID',
  `term_taxonomy_id` bigint NOT NULL COMMENT '栏目ID',
  `term_order` int DEFAULT NULL COMMENT '排序',
  `type` int NOT NULL COMMENT '类型,0:文章内容,1:文章链接，2:栏目链接;类型,0:内容,1:链接',
  PRIMARY KEY (`term_relationships_id`,`term_taxonomy_id`,`type`),
  KEY `Key_1` (`term_relationships_id`,`term_taxonomy_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章栏目关系表;文章栏目关系表';

-- ----------------------------
--  Table structure for `term_taxonomy`
-- ----------------------------
DROP TABLE IF EXISTS `term_taxonomy`;
CREATE TABLE `term_taxonomy` (
  `term_taxonomy_id` bigint NOT NULL COMMENT 'id',
  `description` longtext COLLATE utf8mb4_general_ci COMMENT '说明',
  `name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '栏目名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父栏目id',
  `tpl_path` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板路径',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `attribute` json DEFAULT NULL COMMENT '属性',
  PRIMARY KEY (`term_taxonomy_id`),
  KEY `Key_1` (`term_taxonomy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='栏目;栏目';

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `users_id` bigint NOT NULL COMMENT 'users_id',
  `user_login` varchar(60) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录名',
  `user_pass` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `user_nicename` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `user_email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Email',
  `user_url` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '网址',
  `user_registered` datetime DEFAULT NULL COMMENT '注册时间',
  `user_activation_key` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '激活码',
  `user_status` int DEFAULT NULL COMMENT '用户状态',
  `display_name` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图像',
  `user_type` int DEFAULT NULL COMMENT '用户类型;用户类型 0 :后台 1：前端',
  `open_id` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'open_id',
  `attribute` json DEFAULT NULL COMMENT '属性',
  PRIMARY KEY (`users_id`),
  KEY `Key_1` (`users_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表;用户表';

SET FOREIGN_KEY_CHECKS = 1;
