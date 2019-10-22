package com.company.servlets;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.daos.primitives.AccountDAO;
import com.company.daos.primitives.AccountDAO.AccountTable;

@Component("queries")
public class Queries {

  @Autowired
  private LiveSQL sql;

  public int runLiveSQL() {

    AccountTable a = AccountDAO.newTable("a");

    ExecutableSelect<Map<String, Object>> q = sql //
        .select() //
        .from(a) //
        .where(a.currentBalance.ge(100)) //
    ;

    List<Map<String, Object>> rows = q.execute();

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

    return rows.size();

  }


}
