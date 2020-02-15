## 麻将社区

## 资料

## 工具

## MySQL
    问题： token ---- Data too long for column 'token' at row 1 （可能是字符类型出错，纠正无果）
-- auto-generated definition
create table user
(
    id           int auto_increment
        primary key,
    account_id   varchar(100)            null,
    name         varchar(50) charset gbk null,
    token        varchar(30)             null,
    gmt_create   bigint                  null,
    gmt_modified bigint                  null
);

