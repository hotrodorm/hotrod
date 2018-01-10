package org.hotrod.eclipseplugin.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainConfigFile {

  // Properties

  protected String fullPathName;
  protected String folder;
  protected String shortName;
  protected List<ConfigItem> items = new ArrayList<ConfigItem>();

  // Constructor

  public MainConfigFile(final String fullPathName) {
    super();
    this.fullPathName = fullPathName;
    this.folder = new File(fullPathName).getParent();
    this.shortName = new File(fullPathName).getName();
  }

  // Populate

  public void addConfigItem(final ConfigItem item) {
    this.items.add(item);
  }

  // Getters

  public String getFileName() {
    return this.fullPathName;
  }

  public String getFolder() {
    return folder;
  }

  public String getShortName() {
    return this.shortName;
  }

  public List<ConfigItem> getConfigItems() {
    return this.items;
  }

}
