/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/11 16:00:24                           */
/*==============================================================*/


alter table Consulting
   drop primary key;

drop table if exists Consulting;

alter table Picture
   drop primary key;

drop table if exists Picture;

alter table UserComment
   drop primary key;

drop table if exists UserComment;

alter table "browsing history"
   drop primary key;

drop table if exists "browsing history";

alter table member
   drop primary key;

drop table if exists member;

alter table pictureList
   drop primary key;

drop table if exists pictureList;

alter table power
   drop primary key;

drop table if exists power;

alter table role
   drop primary key;

drop table if exists role;

alter table user
   drop primary key;

drop table if exists user;

/*==============================================================*/
/* Table: Consulting                                            */
/*==============================================================*/
create table Consulting
(
   C_id                 int not null auto_increment,
   C_title              varchar(30) not null,
   C_class              varchar(10),
   C_from               varchar(20),
   C_upTime             datetime,
   C_seeTimes           int,
   C_state              boolean,
   C_note               varchar(500)
);

alter table Consulting
   add primary key (C_id);

/*==============================================================*/
/* Table: Picture                                               */
/*==============================================================*/
create table Picture
(
   P_id                 int not null auto_increment,
   P_class              varchar(10),
   P_frist              varchar(30),
   p_name               varchar(20),
   P_tags               varchar(20),
   P_uptime             datetime,
   P_state              boolean,
   P_picture            varchar(50)
);

alter table Picture
   add primary key (P_id);

/*==============================================================*/
/* Table: UserComment                                           */
/*==============================================================*/
create table UserComment
(
   ID                   integer not null auto_increment,
   m_id                 int(8),
   C_Comments           varchar(100),
   C_Html               varchar(50),
   C_Time               datetime
);

alter table UserComment
   add primary key (ID);

/*==============================================================*/
/* Table: "browsing history"                                    */
/*==============================================================*/
create table "browsing history"
(
   b_id                 int not null auto_increment,
   m_id                 int(8),
   b_ip                 varchar(15) not null,
   b_datatime           datetime,
   b_url                varchar(50) not null
);

alter table "browsing history"
   add primary key (b_id);

/*==============================================================*/
/* Table: member                                                */
/*==============================================================*/
create table member
(
   m_id                 int(8) not null,
   m_username           varchar(5) not null,
   m_sex                varchar(2),
   m_phone              int(15) not null,
   m_email              varchar(20),
   m_location           varchar(30),
   m_data               datetime not null,
   m_statu              varchar(5) not null
);

alter table member
   add primary key (m_id);

/*==============================================================*/
/* Table: pictureList                                           */
/*==============================================================*/
create table pictureList
(
   PL_id                int not null auto_increment,
   P_id                 int,
   PL_note              varchar(100)
);

alter table pictureList
   add primary key (PL_id);

/*==============================================================*/
/* Table: power                                                 */
/*==============================================================*/
create table power
(
   p_id                 int(11) not null,
   detail               varchar(255),
   used                 bool(1)
);

alter table power
   add primary key (p_id);

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   r_id                 int(11) not null,
   p_id                 int(11),
   remarks              varchar(255)
);

alter table role
   add primary key (r_id);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   u_id                 int(11) not null,
   r_id                 int(11),
   username             varchar(255),
   phone                char(11),
   email                varchar(255),
   jointime             time,
   continu              bool(1)
);

alter table user
   add primary key (u_id);

alter table UserComment add constraint FK_Reference_2 foreign key (m_id)
      references member (m_id) on delete restrict on update restrict;

alter table "browsing history" add constraint FK_Reference_6 foreign key (m_id)
      references member (m_id) on delete restrict on update restrict;

alter table pictureList add constraint FK_Reference_5 foreign key (P_id)
      references Picture (P_id) on delete restrict on update restrict;

alter table role add constraint FK_Reference_1 foreign key (p_id)
      references power (p_id) on delete restrict on update restrict;

alter table user add constraint FK_Reference_2 foreign key (r_id)
      references role (r_id) on delete restrict on update restrict;

CREATE TABLE `categories` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`level` INT(11) NULL DEFAULT NULL,
	`from` INT(11) NULL DEFAULT NULL,
	`append` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_categories_categories` (`from`),
	CONSTRAINT `FK_categories_categories` FOREIGN KEY (`from`) REFERENCES `categories` (`id`)
)
ENGINE=InnoDB
;
CREATE TABLE `columns` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`level` INT(11) NULL DEFAULT NULL,
	`from` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_columns_columns` (`from`),
	CONSTRAINT `FK_columns_columns` FOREIGN KEY (`from`) REFERENCES `columns` (`id`)
)
ENGINE=InnoDB
;
CREATE TABLE `column_meta` (
	`column_meta_id` INT(11) NOT NULL,
	`column_id` INT(11) NULL DEFAULT NULL,
	`meta_key` INT(11) NOT NULL,
	`meta_value` INT(11) NOT NULL,
	PRIMARY KEY (`column_meta_id`),
	INDEX `FK_column_meta_columns` (`column_id`),
	CONSTRAINT `FK_column_meta_columns` FOREIGN KEY (`column_id`) REFERENCES `columns` (`id`)
)
ENGINE=InnoDB
;