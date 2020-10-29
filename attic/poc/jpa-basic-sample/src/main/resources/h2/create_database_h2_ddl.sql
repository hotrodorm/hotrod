-- CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;

--  PTY_PERSONA-(1,1)-----(0,n)-TRX_REQUEST-(1,1)-----(0,n)-TRX_REQUEST_ITEM-(0,n)-----(1,1)-STOCK_ARTICULO

create table IF NOT EXISTS PTY_PERSONA (
   PER_ID           BIGSERIAL            not null,
   PER_NOMBRE       VARCHAR(64)          not null,
   PER_APELLIDO     VARCHAR(256)         not null,
   PER_FECHA_NAC    DATE                 not null,
   constraint PK_PTY_PERSONA primary key (PER_ID)
);

create table IF NOT EXISTS TRX_REQUEST (
   REQ_ID           BIGSERIAL			not null,
   REQ_TIMESTAMP    TIMESTAMP			not null,
   REQ_DESC    		VARCHAR(256)		null,
   PER_ID       	BIGINT				not null,
   constraint PK_TRX_REQUEST primary key (REQ_ID)
);

create index IF NOT EXISTS IDX_REQUESTER on TRX_REQUEST (PER_ID);

create table IF NOT EXISTS STOCK_ARTICULO (
   ART_ID           BIGSERIAL			not null,
   ART_DESC    		VARCHAR(256)		not null,
   constraint PK_STOCK_ARTICULO primary key (ART_ID)
);

create table IF NOT EXISTS TRX_REQUEST_ITEM (
   REQ_ID           BIGINT			not null,
   ART_ID           BIGINT			not null,
   ITEM_QTY			INT				not null,
   constraint PK_TRX_REQUEST_ITEM primary key (REQ_ID, ART_ID)
);

alter table TRX_REQUEST
   add constraint IF NOT EXISTS FK_REQUESTER foreign key (PER_ID)
      references PTY_PERSONA (PER_ID)
      on delete restrict on update restrict;

alter table TRX_REQUEST_ITEM
   add constraint IF NOT EXISTS FK_REQUEST foreign key (REQ_ID)
      references TRX_REQUEST (REQ_ID)
      on delete restrict on update restrict;

alter table TRX_REQUEST_ITEM
   add constraint IF NOT EXISTS FK_ARTICULO foreign key (ART_ID)
      references STOCK_ARTICULO (ART_ID)
      on delete restrict on update restrict;
