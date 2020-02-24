## 麻将社区

## 资料

## 工具

## MySQL

-- auto-generated definition
create table user
(
    id           int auto_increment
        primary key,
    account_id   varchar(100) null,
    name         varchar(50)  null,
    token        text         null,
    gmt_create   bigint       null,
    gmt_modified bigint       null
);

# h2
-- auto-generated definition
create table USER
(
    ID           INT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    constraint COMM_PK
        primary key (ID)
);

mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate


