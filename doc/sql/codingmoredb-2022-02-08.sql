drop table if exists comments;

drop table if exists links;

drop table if exists post_tag;

drop table if exists post_tag_relation;

drop table if exists posts;

drop table if exists site;

drop table if exists term_relationships;

drop table if exists term_taxonomy;

drop table if exists users;

/*==============================================================*/
/* Table: comments                                              */
/*==============================================================*/
create table comments
(
   comment_id           bigint(20) unsigned not null auto_increment,
   comment_post_id      bigint(20) unsigned not null default 0 comment '对应文章ID',
   comment_author       tinytext comment '评论者',
   comment_author_email varchar(100) comment '评论者邮箱',
   comment_author_url   varchar(200) comment '评论者网址',
   comment_author_ip    varchar(100) comment '评论者IP',
   comment_date         datetime not null default '0000-00-00 00:00:00' comment '评论时间',
   comment_content      text comment '评论正文',
   comment_approved     varchar(20) comment '评论是否被批准',
   comment_agent        varchar(255) comment '评论者的USER AGENT',
   comment_type         varchar(20) comment '评论类型(pingback/普通)',
   comment_parent       bigint(20) unsigned not null default 0 comment '父评论ID',
   user_id              bigint(20) unsigned not null default 0 comment '评论者用户ID（不一定存在）',
   primary key (comment_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table comments comment '评论表';

/*==============================================================*/
/* Table: links                                                 */
/*==============================================================*/
create table links
(
   link_id              bigint(20) unsigned not null auto_increment,
   link_url             varchar(255) comment '链接URL',
   link_name            varchar(255) comment '链接标题',
   link_image           varchar(255) comment '链接图片',
   link_target          varchar(25) comment '链接打开方式',
   link_description     varchar(255) comment '链接描述',
   link_visible         varchar(20) comment '是否可见（Y/N）',
   link_owner           bigint(20) unsigned not null default 1 comment '添加者用户ID',
   primary key (link_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table links comment '链接信息表';

/*==============================================================*/
/* Table: post_tag                                              */
/*==============================================================*/
create table post_tag
(
   post_tag_id          bigint(20) unsigned not null auto_increment comment 'post_tag_id',
   description          longtext comment '标签名称',
   primary key (post_tag_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table post_tag comment '标签表';

/*==============================================================*/
/* Table: post_tag_relation                                     */
/*==============================================================*/
create table post_tag_relation
(
   post_tag_relation_id bigint(20) unsigned not null default 0 comment '对应文章ID',
   post_tag_id          bigint(20) unsigned not null default 0 comment '标签ID',
   term_order           int(11) not null default 0 comment '排序',
   primary key (post_tag_relation_id, post_tag_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table post_tag_relation comment '标签文章关系表';

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
   posts_id             bigint(20) unsigned not null auto_increment comment 'posts_id',
   post_author          bigint(20) unsigned not null default 0 comment '对应作者ID',
   post_date            datetime not null comment '发布时间',
   post_content         longtext comment '正文',
   post_title           text comment '标题',
   post_excerpt         text comment '摘录',
   post_status          varchar(20) comment '文章状态',
   comment_status       varchar(20) comment '评论状态',
   post_modified        datetime comment '修改时间',
   menu_order           int(11) not null default 0 comment '排序ID',
   post_type            varchar(20) comment '文章类型（post/page等）',
   comment_count        bigint(20) not null default 0 comment '评论总数',
   attribute            JSON comment '属性',
   primary key (posts_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table posts comment '文章';

/*==============================================================*/
/* Table: site                                                  */
/*==============================================================*/
create table site
(
   site_id              bigint(20) not null auto_increment comment 'site_id',
   site_name            varchar(100) comment '名称',
   keywords             varchar(500) comment '关键字',
   site_desc            varchar(1000) comment '介绍',
   attribute            JSON comment '属性',
   update_time          datetime comment '更新时间',
   primary key (site_id)
);

alter table site comment '站点配置';

/*==============================================================*/
/* Table: term_relationships                                    */
/*==============================================================*/
create table term_relationships
(
   term_relationships_id bigint(20) unsigned not null default 0 comment '对应文章ID/链接ID',
   term_taxonomy_id     bigint(20) unsigned not null default 0 comment '栏目ID',
   term_order           int(11) not null default 0 comment '排序',
   type                 int(1) not null comment '类型,0:内容,1:链接',
   primary key (term_relationships_id, term_taxonomy_id, type)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table term_relationships comment '文章栏目关系表';

/*==============================================================*/
/* Table: term_taxonomy                                         */
/*==============================================================*/
create table term_taxonomy
(
   term_taxonomy_id     bigint(20) unsigned not null auto_increment comment 'term_taxonomy_id',
   description          longtext comment '说明',
   name                 varchar(20) not null comment '栏目名称',
   parent_id            bigint(20) unsigned not null default 0 comment '父栏目id',
   create_user_id       bigint(20) comment '创建人id',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   attribute            JSON comment '属性',
   primary key (term_taxonomy_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table term_taxonomy comment '栏目';

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   users_id             bigint(20) unsigned not null auto_increment comment 'users_id',
   user_login           varchar(60) comment '登录名',
   user_pass            varchar(255) comment '密码',
   user_nicename        varchar(50) comment '昵称',
   user_email           varchar(100) comment 'Email',
   user_url             varchar(100) comment '网址',
   user_registered      datetime not null default '0000-00-00 00:00:00' comment '注册时间',
   user_activation_key  varchar(255) comment '激活码',
   user_status          int(11) not null default 0 comment '用户状态',
   display_name         varchar(250) comment '图像',
   user_type            int(1) comment '用户类型 0 :后台 1：前端',
   open_id              varchar(250) comment 'open_id',
   attribute            JSON comment '属性',
   primary key (users_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table users comment '用户表';
