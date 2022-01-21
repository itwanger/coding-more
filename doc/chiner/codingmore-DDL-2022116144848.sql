DROP TABLE IF EXISTS comment_meta;
CREATE TABLE comment_meta(
    meta_id bigint(20)    COMMENT 'meta_id' ,
    comment_id bigint(20)    COMMENT '对应评论ID' ,
    meta_key VARCHAR(255)    COMMENT '键名' ,
    meta_value longtext    COMMENT '键值' ,
    PRIMARY KEY (meta_id)
)  COMMENT = '文章评论额外信息表;文章评论额外信息表';


CREATE INDEX Key_1 ON comment_meta(meta_id);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments(
    comment_id bigint(20)    COMMENT 'comment_ID' ,
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
    site_id bigint(20)    COMMENT '站点id' ,
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
    site_id bigint(20)    COMMENT '站点id' ,
    PRIMARY KEY (link_id)
)  COMMENT = '链接信息表;链接信息表';


CREATE INDEX Key_1 ON links(link_id);

DROP TABLE IF EXISTS postmeta;
CREATE TABLE postmeta(
    meta_id bigint(20)    COMMENT 'ID' ,
    post_id bigint(20)    COMMENT '对应文章ID' ,
    meta_key VARCHAR(255)    COMMENT '键名' ,
    meta_value longtext    COMMENT '键值' ,
    PRIMARY KEY (meta_id)
)  COMMENT = '文章属性表;文章属性表';


CREATE INDEX Key_1 ON postmeta(meta_id);

DROP TABLE IF EXISTS posts;
CREATE TABLE posts(
    id bigint(20)    COMMENT 'ID' ,
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
    site_id bigint(20)    COMMENT '站点id' ,
    PRIMARY KEY (id)
)  COMMENT = '文章;文章';


CREATE INDEX Key_1 ON posts(id);

DROP TABLE IF EXISTS termmeta;
CREATE TABLE termmeta(
    meta_id bigint(20)    COMMENT 'meta_id' ,
    term_taxonomy_id bigint(20)    COMMENT '栏目id;分类id' ,
    meta_key VARCHAR(255)    COMMENT '分类key' ,
    meta_value longtext    COMMENT '分类值' ,
    PRIMARY KEY (meta_id)
)  COMMENT = '栏目属性;栏目属性';


CREATE INDEX Key_1 ON termmeta(meta_id);

DROP TABLE IF EXISTS term_relationships;
CREATE TABLE term_relationships(
    object_id bigint(20)    COMMENT '对应文章ID/链接ID' ,
    term_taxonomy_id bigint(20)    COMMENT '栏目ID' ,
    term_order int(11)    COMMENT '排序' ,
    type int(1)    COMMENT '类型,0:文章内容,1:文章链接，2:栏目链接;类型,0:内容,1:链接' ,
    PRIMARY KEY (object_id,term_taxonomy_id,type)
)  COMMENT = '文章栏目关系表;文章栏目关系表';


CREATE INDEX Key_1 ON term_relationships(object_id,term_taxonomy_id,type);

DROP TABLE IF EXISTS term_taxonomy;
CREATE TABLE term_taxonomy(
    term_taxonomy_id bigint(20)    COMMENT 'id' ,
    description longtext    COMMENT '说明' ,
    name varchar(20)    COMMENT '栏目名称' ,
    parent_id bigint(20)    COMMENT '父栏目id' ,
    site_id bigint(20)    COMMENT '站点id' ,
    tpl_path varchar(200)    COMMENT '模板路径' ,
    create_user_id bigint(20)    COMMENT '创建人id' ,
    create_time DATETIME    COMMENT '创建时间' ,
    update_time DATETIME    COMMENT '更新时间' ,
    content_tpl_path varchar(200)    COMMENT '内容模板路径' ,
    PRIMARY KEY (term_taxonomy_id)
)  COMMENT = '栏目;栏目';


CREATE INDEX Key_1 ON term_taxonomy(term_taxonomy_id);

DROP TABLE IF EXISTS usermeta;
CREATE TABLE usermeta(
    umeta_id bigint(20)    COMMENT 'ID' ,
    user_id bigint(20)    COMMENT '对应用户ID' ,
    meta_key VARCHAR(255)    COMMENT '键名' ,
    meta_value longtext    COMMENT '键值' ,
    PRIMARY KEY (umeta_id)
)  COMMENT = '用户属性;用户属性';


CREATE INDEX Key_1 ON usermeta(umeta_id);

DROP TABLE IF EXISTS users;
CREATE TABLE users(
    id bigint(20)    COMMENT 'ID' ,
    user_login varchar(60)    COMMENT '登录名' ,
    user_pass VARCHAR(255)    COMMENT '密码' ,
    user_nicename varchar(50)    COMMENT '昵称' ,
    user_email varchar(100)    COMMENT 'Email' ,
    user_url varchar(100)    COMMENT '网址' ,
    user_registered DATETIME    COMMENT '注册时间' ,
    user_activation_key VARCHAR(255)    COMMENT '激活码' ,
    user_status int(11)    COMMENT '用户状态' ,
    display_name varchar(250)    COMMENT '显示名称' ,
    user_type int(1)    COMMENT '用户类型;用户类型 0 :后台 1：前端' ,
    PRIMARY KEY (id)
)  COMMENT = '用户表;用户表';


CREATE INDEX Key_1 ON users(id);

DROP TABLE IF EXISTS post_tag;
CREATE TABLE post_tag(
    post_id bigint(20)    COMMENT 'ID' ,
    description longtext    COMMENT '标签名称' ,
    site_id bigint(20)    COMMENT '站点id' ,
    PRIMARY KEY (post_id)
)  COMMENT = '标签表;标签表';


CREATE INDEX Key_1 ON post_tag(post_id);

DROP TABLE IF EXISTS post_tag_relation;
CREATE TABLE post_tag_relation(
    object_id bigint(20)    COMMENT '对应文章ID' ,
    post_tag_id bigint(20)    COMMENT '标签ID' ,
    term_order int(11)    COMMENT '排序' ,
    PRIMARY KEY (object_id,post_tag_id)
)  COMMENT = '标签文章关系表;标签文章关系表';


CREATE INDEX Key_1 ON post_tag_relation(object_id,post_tag_id);

