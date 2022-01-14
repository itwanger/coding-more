drop table if exists SITE;

drop table if exists comment_meta;

drop table if exists comments;

drop table if exists links;

drop table if exists post_tag;

drop table if exists post_tag_relation;

drop table if exists postmeta;

drop table if exists posts;

drop table if exists site_options;

drop table if exists term_relationships;

drop table if exists term_taxonomy;

drop table if exists termmeta;

drop table if exists user_site;

drop table if exists usermeta;

drop table if exists users;

/*==============================================================*/
/* Table: SITE                                                  */
/*==============================================================*/
create table SITE
(
   SITE_ID              bigint(20) not null auto_increment,
   SITE_NAME            varchar(200),
   SITE_DESC            varchar(1000),
   DOMAIN               varchar(200),
   TEL_NAME             varchar(200),
   STATIC_DIR           varchar(200),
   primary key (SITE_ID)
);

alter table SITE comment '站点';

/*==============================================================*/
/* Table: comment_meta                                          */
/*==============================================================*/
create table comment_meta
(
   meta_id              bigint(20) unsigned not null auto_increment,
   comment_id           bigint(20) unsigned not null default 0 comment '对应评论ID',
   meta_key             varchar(255) comment '键名',
   meta_value           longtext comment '键值',
   primary key (meta_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table comment_meta comment '文章评论额外信息表';

/*==============================================================*/
/* Table: comments                                              */
/*==============================================================*/
create table comments
(
   comment_ID           bigint(20) unsigned not null auto_increment,
   comment_post_ID      bigint(20) unsigned not null default 0 comment '对应文章ID',
   comment_author       tinytext comment '评论者',
   comment_author_email varchar(100) comment '评论者邮箱',
   comment_author_url   varchar(200) comment '评论者网址',
   comment_author_IP    varchar(100) comment '评论者IP',
   comment_date         datetime not null default '0000-00-00 00:00:00' comment '评论时间',
   comment_content      text comment '评论正文',
   comment_approved     varchar(20) comment '评论是否被批准',
   comment_agent        varchar(255) comment '评论者的USER AGENT',
   comment_type         varchar(20) comment '评论类型(pingback/普通)',
   comment_parent       bigint(20) unsigned not null default 0 comment '父评论ID',
   user_id              bigint(20) unsigned not null default 0 comment '评论者用户ID（不一定存在）',
   site_id              bigint(20) comment '站点id',
   primary key (comment_ID)
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
   site_id              bigint(20) comment '站点id',
   primary key (link_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table links comment '链接信息表';

/*==============================================================*/
/* Table: post_tag                                              */
/*==============================================================*/
create table post_tag
(
   post_id              bigint(20) unsigned not null auto_increment comment 'ID',
   description          longtext comment '标签名称',
   site_id              bigint(20) comment '站点id',
   primary key (post_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table post_tag comment '标签表';

/*==============================================================*/
/* Table: post_tag_relation                                     */
/*==============================================================*/
create table post_tag_relation
(
   object_id            bigint(20) unsigned not null default 0 comment '对应文章ID',
   post_tag_id          bigint(20) unsigned not null default 0 comment '标签ID',
   term_order           int(11) not null default 0 comment '排序',
   primary key (object_id, post_tag_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table post_tag_relation comment '标签文章关系表';

/*==============================================================*/
/* Table: postmeta                                              */
/*==============================================================*/
create table postmeta
(
   meta_id              bigint(20) unsigned not null auto_increment comment 'ID',
   post_id              bigint(20) unsigned not null default 0 comment '对应文章ID',
   meta_key             varchar(255) comment '键名',
   meta_value           longtext comment '键值',
   primary key (meta_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table postmeta comment '文章属性表';

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
   ID                   bigint(20) unsigned not null auto_increment comment 'ID',
   post_author          bigint(20) unsigned not null default 0 comment '对应作者ID',
   post_date            datetime not null default '0000-00-00 00:00:00' comment '发布时间',
   post_content         longtext comment '正文',
   post_title           text comment '标题',
   post_excerpt         text comment '摘录',
   post_status          varchar(20) comment '文章状态',
   comment_status       varchar(20) comment '评论状态',
   post_modified        datetime not null default '0000-00-00 00:00:00' comment '修改时间',
   menu_order           int(11) not null default 0 comment '排序ID',
   post_type            varchar(20) comment '文章类型（post/page等）',
   comment_count        bigint(20) not null default 0 comment '评论总数',
   site_id              bigint(20) comment '站点id',
   primary key (ID)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table posts comment '文章';

/*==============================================================*/
/* Table: site_options                                          */
/*==============================================================*/
create table site_options
(
   option_id            bigint(20) unsigned not null auto_increment comment 'option_id',
   option_name          varchar(191) comment '键名',
   option_value         longtext comment '键值',
   site_id              bigint(20) comment '站点id',
   primary key (option_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table site_options comment '站点属性配置';

/*==============================================================*/
/* Table: term_relationships                                    */
/*==============================================================*/
create table term_relationships
(
   object_id            bigint(20) unsigned not null default 0 comment '对应文章ID/链接ID',
   term_taxonomy_id     bigint(20) unsigned not null default 0 comment '对应分类方法ID',
   term_order           int(11) not null default 0 comment '排序',
   primary key (object_id, term_taxonomy_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table term_relationships comment '文章属性关系表';

/*==============================================================*/
/* Table: term_taxonomy                                         */
/*==============================================================*/
create table term_taxonomy
(
   term_taxonomy_id     bigint(20) unsigned not null auto_increment comment 'ID',
   description          longtext comment '说明',
   parent               bigint(20) unsigned not null default 0 comment '属父分类方法ID',
   count                bigint(20) not null default 0 comment '文章数统计',
   site_id              bigint(20) comment '站点id',
   primary key (term_taxonomy_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table term_taxonomy comment '栏目';

/*==============================================================*/
/* Table: termmeta                                              */
/*==============================================================*/
create table termmeta
(
   meta_id              bigint(20) unsigned not null auto_increment,
   term_taxonomy_id     bigint(20) unsigned not null default 0 comment '分类id',
   meta_key             varchar(255) comment '分类key',
   meta_value           longtext comment '分类值',
   primary key (meta_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table termmeta comment '栏目属性';

/*==============================================================*/
/* Table: user_site                                             */
/*==============================================================*/
create table user_site
(
   user_id              bigint(20) comment '用户id',
   site_id              bigint(20) comment '站点id'
);

alter table user_site comment '用户站点关联关系表';

/*==============================================================*/
/* Table: usermeta                                              */
/*==============================================================*/
create table usermeta
(
   umeta_id             bigint(20) unsigned not null auto_increment comment 'ID',
   user_id              bigint(20) unsigned not null default 0 comment '对应用户ID',
   meta_key             varchar(255) comment '键名',
   meta_value           longtext comment '键值',
   primary key (umeta_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table usermeta comment '用户属性';

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   ID                   bigint(20) unsigned not null auto_increment comment 'ID',
   user_login           varchar(60) comment '登录名',
   user_pass            varchar(255) comment '密码',
   user_nicename        varchar(50) comment '昵称',
   user_email           varchar(100) comment 'Email',
   user_url             varchar(100) comment '网址',
   user_registered      datetime not null default '0000-00-00 00:00:00' comment '注册时间',
   user_activation_key  varchar(255) comment '激活码',
   user_status          int(11) not null default 0 comment '用户状态',
   display_name         varchar(250) comment '显示名称',
   user_type            int(1) comment '用户类型 0 :后台 1：前端',
   primary key (ID)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

alter table users comment '用户表';
