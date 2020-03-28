package org.hotrod.domain;

import java.io.File;

import org.hotrod.eclipseplugin.FaceProducer.RelativeProjectPath;

public class MainConfigFile extends ConfigItem {

  // Properties

  protected RelativeProjectPath relativeProjectPath;
  protected String fullPathName;
  protected String folder;
  protected String shortName;

  // Constructor

  public MainConfigFile(final File f, final RelativeProjectPath relativeProjectPath) {
    super(0);
    initialize(f, relativeProjectPath);
  }

  public MainConfigFile(final File f, final RelativeProjectPath relativeProjectPath, final int lineNumber) {
    super(lineNumber);
    initialize(f, relativeProjectPath);
  }

  private void initialize(final File f, final RelativeProjectPath relativeProjectPath) {
    this.relativeProjectPath = relativeProjectPath;
    this.fullPathName = f.getAbsolutePath();
    this.folder = f.getParent();
    this.shortName = f.getName();
  }

  // Populate

  public void addConfigItem(final ConfigItem item) {
    this.getSubItems().add(item);
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

  // Compute item changes

  @Override
  public boolean sameID(final ConfigItem fresh) {
    return true;
  }

  // Copy non-ID properties; informs if there were any changes.
  @Override
  public boolean copyProperties(final ConfigItem fresh) {
    return false;
  }

}
