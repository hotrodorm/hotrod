package com.myapp1.tes;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

import com.myapp1.persistence.primitives.AccountDAO;
import com.myapp1.persistence.primitives.AccountDAO.AccountTable;

public class Subquery1 {

//  public ExecutableSelect<?> getSubquery1() {
//    AccountTable a = AccountDAO.newTable("a");
//    return LiveSQL.select( //
//        a.name, //
//        a.currentBalance, //
//        a.currentBalance.plus(a.id).as("adjustedBalance") //
//    ).from(a);
//  }

}
