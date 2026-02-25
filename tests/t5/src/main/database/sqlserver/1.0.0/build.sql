create table branch (
  id int primary key not null,
  [NaMe] varchar(20),
  region varchar(10),
  is_vip int
);

insert into branch (id, [NaMe], region) values (101, 'SOUTH', 'SOUTH'), (102, 'East', 'EAST');

create table invoice (
  id int primary key not null,
  amount int,
  branch_id int references branch (id),
  status varchar(10),
  order_date date,
  unpaid_balance int,
  account_id int,
  type varchar(10)
);

create table "CASE" (
  id int primary key not null,
  name varchar(50)
);

insert into invoice (id, amount, branch_id, status) values (10, 1500, 101, 'ABC'), (11, 2500, 101, 'DEF'), (12, 4000, 102, 'GHI');

-- Test multi-result set

-- @delimiter // solo

create procedure dualselect
@a integer
as
begin
  select @a + 2;
  select @a + 4;
end
//

create table account (
  id int identity primary key not null,
  name varchar(20) not null,
  type varchar(3) not null,
  balance int not null
);

insert into account (name, type, balance) values
  ('1010', 'CHK', 100),
  ('2055', 'SAV', 200),
  ('1072', 'CHK', 500);

create sequence seq_account start with 1000;



-- generated keys

create table data (
  name varchar(20)
);

insert into data (name) values ('Alice');
insert into data (name) values ('Anne');
insert into data (name) values ('Alanis');

create table k1 (
  id tinyint identity(40, 1) primary key not null,
  name varchar(20)
);

create table k2 (
  id smallint identity(50, 1) primary key not null,
  name varchar(20)
);

create table k3 (
  id int identity(60, 1) primary key not null,
  name varchar(20)
);

create table k4 (
  id bigint identity(70, 1) primary key not null,
  name varchar(20)
);

create sequence seq1 start with 51;

create table s1 (
  id tinyint primary key,
  name varchar(20)
);

create table s2 (
  id smallint primary key,
  name varchar(20)
);

create table s3 (
  id int primary key,
  name varchar(20)
);

create table s4 (
  id bigint primary key,
  name varchar(20)
);

create table t5 (
  id bigint primary key,
  name varchar(20)
);

-- blob

create table person (
  id bigint primary key not null,
  photo varbinary(1000)
);

insert into person (id, photo) values 
  (1, 0x89504e470d0a1a0a0000000d4948445200000020000000200806000000737a7af4000000097048597300000b1300000b1301009a9c180000008f49444154789ced94b10d80300c04bd042d2b50b3091b502165908805588b11be6487d0d080b0149c0427924ffaf67d4a641319460ba01f8224ed0be02a9ab65d946411fc2500e6e9720bb05f84da0490389813a957e04929018a055a02101e9c6c070ab5088463bd453a88eb3101aa5e004c7c3746a5d816786d01fcb586d01270cb1cde42423ef7396d01c320054ee50cbdb9cb1324d70000000049454e44ae426082
);


  