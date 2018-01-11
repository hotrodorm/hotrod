package org.hotrod.eclipseplugin.domain;

import java.io.File;

import org.hotrod.eclipseplugin.treeview.FaceProducer.RelativeProjectPath;

public class FragmentConfigFile extends MainConfigFile implements ConfigItem {

  private String includerRelativePath;

  public FragmentConfigFile(final File f, final RelativeProjectPath relativeProjectPath,
      final String includerRelativePath) {
    super(f, relativeProjectPath);
    this.includerRelativePath = includerRelativePath;
  }

  public String getIncluderRelativePath() {
    return includerRelativePath;
  }

}
