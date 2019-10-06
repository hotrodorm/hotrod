package com.company.cli.maven_test;

import java.util.List;

import com.company.cli.maven_test.daos.AccountVolumeVO;

public class App {

  public static void main(final String[] args) {

    App a = new App();
    a.showAccounts();

  }

  public void showAccounts() {
    System.out.println("[ Starting ]");

    DataServices dataSQL = SpringBeanRetriever.getBean("dataServices");

    List<AccountVolumeVO> v = dataSQL.getNewAccountVolume();
    for (AccountVolumeVO av : v) {
      System.out.println("av=" + av);
    }

    System.out.println("[ Completed ]");
  }

}
