DROP TABLE IF EXISTS comments;
CREATE TABLE comments(
    comment_id bigint(20)    COMMENT 'comment_id' ,
    comment_post_id bigint(20)    COMMENT '对应文章ID' ,
    comment_author tinytext    COMMENT '评论者' ,
    comment_author_email varchar(100)    COMMENT '评论者邮箱' ,
    comment_author_url varchar(200)    COMMENT '评论者网址' ,
    comment_author_ip varchar(100)    COMMENT '评论者IP' ,
    comment_date DATETIME    COMMENT '评论时间' ,
    comment_content text    COMMENT '评论正文' ,
    comment_approved varchar(20)    COMMENT '评论是否被批准' ,
    comment_agent VARCHAR(255)    COMMENT '评论者的USER AGENT' ,
    comment_type varchar(20)    COMMENT '评论类型(pingback/普通)' ,
    comment_parent bigint(20)    COMMENT '父评论ID' ,
    user_id bigint(20)    COMMENT '评论者用户ID（不一定存在）' ,
    PRIMARY KEY (comment_id)
)  COMMENT = '评论表;评论表';


CREATE INDEX Key_1 ON comments(comment_id);

DROP TABLE IF EXISTS links;
CREATE TABLE links(
    link_id bigint(20)    COMMENT 'link_id' ,
    link_url VARCHAR(255)    COMMENT '链接URL' ,
    link_name VARCHAR(255)    COMMENT '链接标题' ,
    link_image VARCHAR(255)    COMMENT '链接图片' ,
    link_target varchar(25)    COMMENT '链接打开方式' ,
    link_description VARCHAR(255)    COMMENT '链接描述' ,
    link_visible varchar(20)    COMMENT '是否可见（Y/N）' ,
    link_owner bigint(20)    COMMENT '添加者用户ID' ,
    PRIMARY KEY (link_id)
)  COMMENT = '链接信息表;链接信息表';


CREATE INDEX Key_1 ON links(link_id);

DROP TABLE IF EXISTS posts;
CREATE TABLE posts(
    posts_id bigint(20)    COMMENT 'posts_id' ,
    post_author bigint(20)    COMMENT '对应作者ID' ,
    post_date DATETIME    COMMENT '发布时间' ,
    post_content longtext    COMMENT '正文' ,
    post_title text    COMMENT '标题' ,
    post_excerpt text    COMMENT '摘录' ,
    post_status varchar(20)    COMMENT '文章状态' ,
    comment_status varchar(20)    COMMENT '评论状态' ,
    post_modified DATETIME    COMMENT '修改时间' ,
    menu_order int(11)    COMMENT '排序ID' ,
    post_type varchar(20)    COMMENT '文章类型（post/page等）' ,
    comment_count bigint(20)    COMMENT '评论总数' ,
    attribute JSON    COMMENT '属性' ,
    PRIMARY KEY (posts_id)
)  COMMENT = '文章;文章';


CREATE INDEX Key_1 ON posts(posts_id);

DROP TABLE IF EXISTS term_relationships;
CREATE TABLE term_relationships(
    term_relationships_id bigint(20)    COMMENT '对应文章ID/链接ID' ,
    term_taxonomy_id bigint(20)    COMMENT '栏目ID' ,
    term_order int(11)    COMMENT '排序' ,
    type int(1)    COMMENT '类型,0:文章内容,1:文章链接，2:栏目链接;类型,0:内容,1:链接' ,
    PRIMARY KEY (term_relationships_id,term_taxonomy_id,type)
)  COMMENT = '文章栏目关系表;文章栏目关系表';


CREATE INDEX Key_1 ON term_relationships(term_relationships_id,term_taxonomy_id,type);

DROP TABLE IF EXISTS term_taxonomy;
CREATE TABLE term_taxonomy(
    term_taxonomy_id bigint(20)    COMMENT 'id' ,
    description longtext    COMMENT '说明' ,
    name varchar(20)    COMMENT '栏目名称' ,
    parent_id bigint(20)    COMMENT '父栏目id' ,
    create_user_id bigint(20)    COMMENT '创建人id' ,
    create_time DATETIME    COMMENT '创建时间' ,
    update_time DATETIME    COMMENT '更新时间' ,
    attribute JSON    COMMENT '属性' ,
    PRIMARY KEY (term_taxonomy_id)
)  COMMENT = '栏目;栏目';


CREATE INDEX Key_1 ON term_taxonomy(term_taxonomy_id);

DROP TABLE IF EXISTS users;
CREATE TABLE users(
    users_id bigint(20)    COMMENT 'users_id' ,
    user_login varchar(60)    COMMENT '登录名' ,
    user_pass VARCHAR(255)    COMMENT '密码' ,
    user_nicename varchar(50)    COMMENT '昵称' ,
    user_email varchar(100)    COMMENT 'Email' ,
    user_url varchar(100)    COMMENT '网址' ,
    user_registered DATETIME    COMMENT '注册时间' ,
    user_activation_key VARCHAR(255)    COMMENT '激活码' ,
    user_status int(11)    COMMENT '用户状态' ,
    display_name varchar(250)    COMMENT '图像' ,
    user_type int(1)    COMMENT '用户类型;用户类型 0 :后台 1：前端' ,
    open_id varchar(250)    COMMENT 'open_id' ,
    attribute JSON    COMMENT '属性' ,
    PRIMARY KEY (users_id)
)  COMMENT = '用户表;用户表';


CREATE INDEX Key_1 ON users(users_id);

DROP TABLE IF EXISTS post_tag;
CREATE TABLE post_tag(
    post_id bigint(20)    COMMENT 'ID' ,
    description longtext    COMMENT '标签名称' ,
    PRIMARY KEY (post_id)
)  COMMENT = '标签表;标签表';


CREATE INDEX Key_1 ON post_tag(post_id);

DROP TABLE IF EXISTS post_tag_relation;
CREATE TABLE post_tag_relation(
    post_tag_relation_id bigint(20)    COMMENT '对应文章ID' ,
    post_tag_id bigint(20)    COMMENT '标签ID' ,
    term_order int(11)    COMMENT '排序' ,
    PRIMARY KEY (post_tag_relation_id,post_tag_id)
)  COMMENT = '标签文章关系表;标签文章关系表';


CREATE INDEX Key_1 ON post_tag_relation(post_tag_relation_id,post_tag_id);

DROP TABLE IF EXISTS site;
CREATE TABLE site(
    site_id bigint(20)    COMMENT 'site_id' ,
    site_name varchar(100)    COMMENT '名称' ,
    keywords varchar(500)    COMMENT '关键字' ,
    site_desc varchar(1000)    COMMENT '介绍' ,
    attribute JSON    COMMENT '属性' ,
    update_time DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (site_id)
)  COMMENT = '站点配置;站点配置';


CREATE INDEX Key_1 ON site(site_id);

