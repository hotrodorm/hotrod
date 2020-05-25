package com.myapp1.model;

import java.util.List;

import com.myapp1.persistence.AccountVolumeVO;

public class App {

  public static void main(final String[] args) {

    App a = new App();
    a.showAccounts();

  }

  public void showAccounts() {
    System.out.println("[ Starting ]");

    DataServices dataSQL = SpringBeanRetriever.getBean("dataServices");

    List<AccountVolumeVO> v = dataSQL.getNewAccountVolume();
    System.out.println("[ rows = " + v.size() + " ]");
    for (AccountVolumeVO av : v) {
      System.out.println("av=" + av);
    }

    System.out.println("[ Completed ]");
  }

  public void retrieveBigAccounts() {
    DataServices dataSQL = SpringBeanRetriever.getBean("dataServices");
    dataSQL.retrieveBigAccounts();
  }

}
