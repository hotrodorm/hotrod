package org.hotrod.eclipseplugin.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainConfigFile {

  // Properties

  protected String fileName;
  protected String shortName;
  protected List<ConfigItem> items = new ArrayList<ConfigItem>();

  // Constructor

  public MainConfigFile(final String fileName) {
    super();
    this.fileName = fileName;
    this.shortName = new File(fileName).getName();
  }

  // Populate

  public void addConfigItem(final ConfigItem item) {
    this.items.add(item);
  }

  // Getters

  public String getFileName() {
    return this.fileName;
  }

  public String getShortName() {
    return this.shortName;
  }

  public List<ConfigItem> getConfigItems() {
    return this.items;
  }

}
