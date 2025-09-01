/*==============================================================*/
/* Table: KZO_ACTION                                            */
/*==============================================================*/
create table IF NOT EXISTS KZO_ACTION (
   ACTION_ID            BIGSERIAL            not null,
   ACTION_TITLE         VARCHAR(64)          not null,
   ACTION_SUB_TITLE     VARCHAR(256)         null,
   constraint PK_KZO_ACTION primary key (ACTION_ID)
);


/*==============================================================*/
/* Table: KZO_NEWS                                              */
/*==============================================================*/
create table IF NOT EXISTS KZO_NEWS (
   ACTION_ID            BIGINT               not null,
   NEWS_DESCRIPTION     VARCHAR(1000)        not null,
   NEWS_METADATA        VARCHAR(512)         null,
   constraint PK_KZO_NEWS primary key (ACTION_ID)
);


/*==============================================================*/
/* Table: KZO_POLL                                              */
/*==============================================================*/
create table IF NOT EXISTS KZO_POLL (
   ACTION_ID            BIGINT               not null,
   POLL_DESCRIPTION     VARCHAR(1000)        not null,
   POLL_METADATA        VARCHAR(512)         null,
   constraint PK_KZO_POLL primary key (ACTION_ID)
);

alter table KZO_NEWS
   add constraint IF NOT EXISTS FK_KZO_NEWS_REFERENCE_KZO_ACTI foreign key (ACTION_ID)
      references KZO_ACTION (ACTION_ID)
      on delete restrict on update restrict;

alter table KZO_POLL
   add constraint IF NOT EXISTS FK_KZO_POLL_REFERENCE_KZO_ACTI foreign key (ACTION_ID)
      references KZO_ACTION (ACTION_ID)
      on delete restrict on update restrict;

