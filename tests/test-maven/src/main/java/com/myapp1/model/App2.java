package com.myapp1.model;

import java.sql.SQLException;

public class App2 {

  public static void main(final String[] args) throws SQLException {
    App2 a = new App2();
    a.test1();
  }

  public void test1() throws SQLException {
    System.out.println("[ Starting ]");
    DataServices2 s2 = SpringBeanRetriever.getBean("dataServices2");

    s2.demoFKs();

    System.out.println("[ Completed ]");
  }

}
