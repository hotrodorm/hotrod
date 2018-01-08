package org.hotrod.eclipseplugin.domain;

import java.util.ArrayList;
import java.util.List;

public class MainConfigFile {

  // Properties

  protected String fileName;
  protected List<ConfigItem> items = new ArrayList<ConfigItem>();

  // Constructor

  public MainConfigFile(final String fileName) {
    super();
    this.fileName = fileName;
  }

  // Populate

  public void addConfigItem(final ConfigItem item) {
    this.items.add(item);
  }

  // Getters

  public String getFileName() {
    return fileName;
  }

  public List<ConfigItem> getConfigItems() {
    return this.items;
  }

}
