# The `<classic-fk-navigation>` Tag

This tag enables the MyBatis-Spring generator to produce CRUD methods to navigate from one VO to 
related VOs by the use of foreign keys in the tables.

Since in earlier versions of HotRod there were collisions in the naming convention, it could be sometimes
useful to disable these methods. This is not an issue anymore, so there's no reason not to enable them anymore.

For example, if there are two tables related by foreign keys as shown below:

```sql
create table account (
  id int primary key not null,
  balance int not null
);

create table money_transfer (
  src_account int not null references account (id),
  dest_account int not null references account (id),
  amount int not null,
  transfer_date timestamp not null
);
```

The MyBatis-Spring generator will produce two AOs (`AccountDAO` and `MoneyTransfer`) and two 
VOs (`AcountVO` and `MoneyTransferVO`).

Since there are two foreign key constraint between these tables, the `AccountDAO` will include the following methods:
- `List<MoneyTransferVO> selectChildrenMoneyTransferOf(AccountVO).fromSrcAccount().toId()`
- `List<MoneyTransferVO> selectChildrenMoneyTransferOf(AccountVO).fromDestAccount().toId()`

Since there are two foreign key constraint between these tables, the `MoneyTransferDAO` will include the following methods:
- `AccountVO selectParentAccountOf(MoneyTransferVO).fromSrcAccount().toId()`.
- `AccountVO selectParentAccountOf(MoneyTransferVO).fromDestAccount().toId()`.


