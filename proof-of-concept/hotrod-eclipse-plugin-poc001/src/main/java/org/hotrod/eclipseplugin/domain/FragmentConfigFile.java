package org.hotrod.eclipseplugin.domain;

import java.io.File;

import org.hotrod.eclipseplugin.domain.loader.FaceProducer.RelativeProjectPath;

public class FragmentConfigFile extends MainConfigFile {

  private String includerRelativePath;

  public FragmentConfigFile(final File f, final RelativeProjectPath relativeProjectPath,
      final String includerRelativePath, final int lineNumber) {
    super(f, relativeProjectPath, lineNumber);
    this.includerRelativePath = includerRelativePath;
  }

  public String getIncluderRelativePath() {
    return includerRelativePath;
  }

}
