package org.hotrod.eclipseplugin.domain;

import java.util.ArrayList;
import java.util.List;

public class FragmentConfigFile extends DAO {

  // Properties

  private String fileName;
  private List<DAO> daos = new ArrayList<DAO>();

  // Constructor

  public FragmentConfigFile(final String fileName) {
    super();
    this.fileName = fileName;
  }

  // Populate

  public void addDAO(final DAO d) {
    this.daos.add(d);
  }

  // Getters

  public String getFileName() {
    return fileName;
  }

  public List<DAO> getDAOs() {
    return this.daos;
  }

}
