package org.hotrod.eclipseplugin.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.eclipseplugin.domain.loader.FaceProducer.RelativeProjectPath;

public class MainConfigFile {

  // Properties

  protected RelativeProjectPath relativeProjectPath;
  protected String fullPathName;
  protected String folder;
  protected String shortName;
  protected List<ConfigItem> items = new ArrayList<ConfigItem>();

  // Constructor

  public MainConfigFile(final File f, final RelativeProjectPath relativeProjectPath) {
    super();
    this.relativeProjectPath = relativeProjectPath;
    this.fullPathName = f.getAbsolutePath();
    this.folder = f.getParent();
    this.shortName = f.getName();
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

  public RelativeProjectPath getRelativeProjectPath() {
    return relativeProjectPath;
  }

  public String getShortName() {
    return this.shortName;
  }

  public List<ConfigItem> getConfigItems() {
    return this.items;
  }

}
