package org.hotrod.eclipseplugin.domain;

import java.io.File;

import org.hotrod.eclipseplugin.domain.loader.FaceProducer.RelativeProjectPath;

public class FragmentConfigFile extends MainConfigFile implements ConfigItem {

  private String includerRelativePath;
  private int lineNumber;

  public FragmentConfigFile(final File f, final RelativeProjectPath relativeProjectPath,
      final String includerRelativePath, final int lineNumber) {
    super(f, relativeProjectPath);
    this.includerRelativePath = includerRelativePath;
    this.lineNumber = lineNumber;
  }

  public String getIncluderRelativePath() {
    return includerRelativePath;
  }

  @Override
  public int getLineNumber() {
    return lineNumber;
  }

}
