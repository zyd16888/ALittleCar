/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/14 10:06:34                           */
/*==============================================================*/


drop table if exists Consulting;

drop table if exists Diary;

drop table if exists Picture;

drop table if exists UserComment;

drop table if exists browsing_history;

drop table if exists manager;

drop table if exists member;

drop table if exists pictureList;

drop table if exists power;

drop table if exists roles;

/*==============================================================*/
/* Table: Consulting                                            */
/*==============================================================*/
create table Consulting
(
   C_id                 bigint(20) not null auto_increment,
   C_title              varchar(30) not null,
   C_class              varchar(10),
   C_from               varchar(20),
   C_upTime             datetime,
   C_seeTimes           int,
   C_state              boolean,
   C_note               varchar(500),
   C_create_man         varchar(30),
   C_create_time        datetime,
   C_update_man         varchar(30),
   C_update_time        datetime,
   C_delete_state       char(2),
   primary key (C_id)
);

/*==============================================================*/
/* Table: Diary                                                 */
/*==============================================================*/
create table Diary
(
   id                   bigint(20) not null,
   type                 int(11),
   content              varchar(255),
   admin                varchar(255),
   IP                   varchar(255),
   Time                 datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: Picture                                               */
/*==============================================================*/
create table Picture
(
   P_id                 bigint(20) not null auto_increment,
   P_class              varchar(10),
   P_frist              varchar(30),
   p_name               varchar(20),
   P_tags               varchar(20),
   P_uptime             datetime,
   P_state              boolean,
   P_picture            varchar(50),
   P_create_man         varchar(30),
   P_create_time        datetime,
   P_update_man         varchar(30),
   P_update_time        datetime,
   P_delete_state       char(2),
   primary key (P_id)
);

/*==============================================================*/
/* Table: UserComment                                           */
/*==============================================================*/
create table UserComment
(
   ID                   bigint(20) not null auto_increment,
   m_id                 bigint(20),
   C_Comments           varchar(100),
   C_Html               varchar(50),
   C_Time               datetime,
   primary key (ID)
);

/*==============================================================*/
/* Table: browsing_history                                      */
/*==============================================================*/
create table browsing_history
(
   b_id                 bigint(20) not null auto_increment,
   m_id                 bigint(20),
   b_ip                 varchar(15) not null,
   b_datatime           datetime,
   b_url                varchar(50) not null,
   primary key (b_id)
);

/*==============================================================*/
/* Table: manager                                               */
/*==============================================================*/
create table manager
(
   u_id                 bigint(20) not null auto_increment,
   r_id                 bigint(20),
   username             varchar(255),
   password             varchar(20),
   sex                  char(2),
   phone                char(11),
   email                varchar(255),
   create_man           varchar(30),
   jointime             time,
   update_man           varchar(30),
   update_time          datetime,
   state                boolean,
   mark                 char(255),
   delete_state         char(2),
   primary key (u_id)
);

/*==============================================================*/
/* Table: member                                                */
/*==============================================================*/
create table member
(
   m_id                 bigint(20) not null,
   m_username           varchar(5) not null,
   m_sex                varchar(2),
   m_phone              int(15) not null,
   m_email              varchar(20),
   m_location           varchar(30),
   create_man           varchar(30),
   m_data               datetime not null,
   update_man           varchar(30),
   update_time          datetime,
   m_statu              varchar(5) not null,
   delete_state         char(2),
   primary key (m_id)
);

/*==============================================================*/
/* Table: pictureList                                           */
/*==============================================================*/
create table pictureList
(
   PL_id                integer(20) not null auto_increment,
   P_id                 bigint(20),
   PL_note              varchar(100),
   primary key (PL_id)
);

/*==============================================================*/
/* Table: power                                                 */
/*==============================================================*/
create table power
(
   power_id             bigint(20) not null,
   detail               varchar(255),
   used                 boolean,
   create_man           varchar(30),
   create_time          datetime,
   update_man           varchar(30),
   update_time          datetime,
   delete_state         char(2),
   primary key (power_id)
);

/*==============================================================*/
/* Table: roles                                                 */
/*==============================================================*/
create table roles
(
   r_id                 bigint(20) not null auto_increment,
   power_id             bigint(20),
   mark                 varchar(255),
   name                 varchar(255),
   create_man           varchar(30),
   create_time          datetime,
   update_man           varchar(30),
   update_time          datetime,
   delete_state         char(2),
   primary key (r_id)
);

alter table UserComment add constraint FK_Reference_8 foreign key (m_id)
      references member (m_id) on delete restrict on update restrict;

alter table browsing_history add constraint FK_Reference_6 foreign key (m_id)
      references member (m_id) on delete restrict on update restrict;

alter table manager add constraint FK_Reference_4 foreign key (r_id)
      references roles (r_id) on delete restrict on update restrict;

alter table pictureList add constraint FK_Reference_5 foreign key (P_id)
      references Picture (P_id) on delete restrict on update restrict;

alter table roles add constraint FK_Reference_7 foreign key (power_id)
      references power (power_id) on delete restrict on update restrict;

CREATE TABLE `categories` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`level` INT(11) NULL DEFAULT NULL,
	`from` INT(11) NULL DEFAULT NULL,
	`append` VARCHAR(50) NULL DEFAULT NULL,
	`create_by_bight` VARCHAR(50) NULL DEFAULT NULL,
	`create time` DATE NULL DEFAULT NULL,
	`update_by_bight` VARCHAR(50) NULL DEFAULT NULL,
	`update_time` DATE NULL DEFAULT NULL,
	`delete_fliag` TINYINT(4) NULL DEFAULT '0',
	PRIMARY KEY (`id`),
	INDEX `FK_categories_categories` (`from`),
	CONSTRAINT `FK_categories_categories` FOREIGN KEY (`from`) REFERENCES `categories` (`id`)
)
;

CREATE TABLE `columns` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`level` INT(11) NULL DEFAULT NULL,
	`from` INT(11) NULL DEFAULT NULL,
	`create_by_bight` VARCHAR(50) NULL DEFAULT NULL,
	`create time` DATE NULL DEFAULT NULL,
	`update_by_bight` VARCHAR(50) NULL DEFAULT NULL,
	`update_time` DATE NULL DEFAULT NULL,
	`delete_fliag` TINYINT(4) NULL DEFAULT '0',
	PRIMARY KEY (`id`),
	INDEX `FK_columns_columns` (`from`),
	CONSTRAINT `FK_columns_columns` FOREIGN KEY (`from`) REFERENCES `columns` (`id`)
)
;
CREATE TABLE `column_meta` (
	`column_meta_id` INT(11) NOT NULL,
	`column_id` INT(11) NULL DEFAULT NULL,
	`meta_key` INT(11) NOT NULL,
	`meta_value` INT(11) NOT NULL,
	`create_by_bight` VARCHAR(50) NULL DEFAULT NULL,
	`create time` DATE NULL DEFAULT NULL,
	`update_by_bight` VARCHAR(50) NULL DEFAULT NULL,
	`update_time` DATE NULL DEFAULT NULL,
	`delete_fliag` TINYINT(4) NULL DEFAULT '0',
	PRIMARY KEY (`column_meta_id`),
	INDEX `FK_column_meta_columns` (`column_id`),
	CONSTRAINT `FK_column_meta_columns` FOREIGN KEY (`column_id`) REFERENCES `columns` (`id`)
)
;
