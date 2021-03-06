# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table batch (
  batch_id                      bigint auto_increment not null,
  batch_name                    varchar(255),
  description                   varchar(255),
  batch_incharge_name           varchar(255),
  batch_incharge_email          varchar(255),
  batch_incharge_phone          varchar(255),
  school_id                     bigint,
  created_by                    bigint,
  logo                          varchar(255),
  constraint pk_batch primary key (batch_id)
);

create table comments (
  comment_id                    bigint auto_increment not null,
  subject                       varchar(255),
  comment_by_name               varchar(255),
  description                   text,
  attachment                    varchar(255),
  start_date                    datetime(6),
  end_date                      datetime(6),
  actual_end_date               datetime(6),
  marked_flag                   integer not null,
  projectid                     bigint not null,
  constraint pk_comments primary key (comment_id)
);

create table notification (
  notification_id               bigint auto_increment not null,
  notification_type             integer,
  notification_content_type     integer,
  title                         varchar(255),
  message                       varchar(255),
  to_address                    varchar(255),
  notification_initiation_dt    datetime(6),
  notification_sent_dt          datetime(6),
  constraint pk_notification primary key (notification_id)
);

create table password (
  pwd_id                        bigint auto_increment not null,
  password                      varchar(255),
  email                         varchar(255),
  userkey                       bigint not null,
  constraint uq_password_userkey unique (userkey),
  constraint pk_password primary key (pwd_id)
);

create table project (
  project_id                    bigint auto_increment not null,
  project_title                 varchar(255),
  project_title_desc            varchar(255),
  logo                          varchar(255),
  description                   text,
  project_creation_dt           datetime(6),
  duration                      integer,
  school_id                     bigint not null,
  created_by                    bigint not null,
  student_id                    bigint not null,
  final_description             varchar(255),
  zipped_comments               varchar(255),
  constraint pk_project primary key (project_id)
);

create table school (
  school_id                     bigint auto_increment not null,
  school_name                   varchar(255),
  description                   varchar(255),
  contact_person_name           varchar(255),
  contact_person_email          varchar(255),
  contact_person_phone          varchar(255),
  logo                          varchar(255),
  constraint pk_school primary key (school_id)
);

create table section (
  section_id                    bigint auto_increment not null,
  section_name                  varchar(255),
  description                   varchar(255),
  section_incharge_name         varchar(255),
  section_incharge_email        varchar(255),
  section_incharge_phone        varchar(255),
  school_id                     bigint,
  created_by                    bigint,
  constraint pk_section primary key (section_id)
);

create table sub_comments (
  sub_comment_id                bigint auto_increment not null,
  subject                       varchar(255),
  comment_by_name               varchar(255),
  description                   text,
  attachment                    varchar(255),
  start_date                    datetime(6),
  end_date                      datetime(6),
  actual_end_date               datetime(6),
  marked_flag                   integer not null,
  commentid                     bigint not null,
  constraint pk_sub_comments primary key (sub_comment_id)
);

create table user (
  user_key                      bigint auto_increment not null,
  user_id                       varchar(255),
  username                      varchar(255),
  student_id                    varchar(255),
  project_status                integer,
  student_level                 integer,
  email                         varchar(255),
  phone                         varchar(255),
  logo                          varchar(255),
  role_type                     integer,
  password_generation_status    integer default 0,
  password_reference            varchar(255),
  school_id                     integer default 0,
  batch_id                      integer default 0,
  section_id                    integer default 0,
  created_by                    integer default 0,
  constraint uq_user_user_id unique (user_id),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (user_key)
);

create table user_role (
  role_id                       bigint auto_increment not null,
  role_name                     varchar(255),
  role_type                     integer,
  role_description              varchar(255),
  userkey                       bigint,
  constraint uq_user_role_userkey unique (userkey),
  constraint pk_user_role primary key (role_id)
);

alter table comments add constraint fk_comments_projectid foreign key (projectid) references project (project_id) on delete restrict on update restrict;
create index ix_comments_projectid on comments (projectid);

alter table password add constraint fk_password_userkey foreign key (userkey) references user (user_key) on delete restrict on update restrict;

alter table sub_comments add constraint fk_sub_comments_commentid foreign key (commentid) references comments (comment_id) on delete restrict on update restrict;
create index ix_sub_comments_commentid on sub_comments (commentid);

alter table user_role add constraint fk_user_role_userkey foreign key (userkey) references user (user_key) on delete restrict on update restrict;


# --- !Downs

alter table comments drop foreign key fk_comments_projectid;
drop index ix_comments_projectid on comments;

alter table password drop foreign key fk_password_userkey;

alter table sub_comments drop foreign key fk_sub_comments_commentid;
drop index ix_sub_comments_commentid on sub_comments;

alter table user_role drop foreign key fk_user_role_userkey;

drop table if exists batch;

drop table if exists comments;

drop table if exists notification;

drop table if exists password;

drop table if exists project;

drop table if exists school;

drop table if exists section;

drop table if exists sub_comments;

drop table if exists user;

drop table if exists user_role;

