/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/6/11 14:23:44                           */
/*==============================================================*/


drop table if exists Consulting;

drop table if exists Picture;

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
   C_operation          char(10),
   C_choice             boolean,
   primary key (C_id)
);

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
   P_operation          char(10),
   P_choice             boolean,
   primary key (P_id)
);

