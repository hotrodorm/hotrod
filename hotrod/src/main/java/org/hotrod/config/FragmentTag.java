package org.hotrod.config;

import java.io.File;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class FragmentTag {

  private static final Logger log = Logger.getLogger(FragmentTag.class);

  static final String TAG_NAME = "fragment";

  private String filename = null;

  private File file;
  private HotRodFragmentConfigTag fragmentConfig;

  public void validate(final HotRodConfigTag config, final File baseDir, final Set<String> alreadyLoadedFileNames,
      final File parentFile, final DaosTag daosTag)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException {

    log.debug("init");

    // file

    if (SUtils.isEmpty(this.filename)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'file' of tag <" + TAG_NAME + "> cannot be empty. " + "You must specify a file name.");
    }
    this.file = new File(baseDir, this.filename);
    if (!this.file.exists()) {
      throw new InvalidConfigurationFileException(
          "Could not find fragment file '" + this.file.getAbsolutePath() + "'.");
    }
    if (!this.file.isFile()) {
      throw new InvalidConfigurationFileException("Invalid fragment file '" + this.file.getAbsolutePath()
          + "'. Must be a normal file, not a directory or other special file.");
    }

    this.fragmentConfig = new HotRodFragmentConfigTag().load(config, this.file, alreadyLoadedFileNames, parentFile,
        daosTag);

  }

  // Setters (digester)

  public void setFile(final String filename) {
    this.filename = filename;
  }

  // Getters

  public String getFilename() {
    return filename;
  }

  public HotRodFragmentConfigTag getConfig() {
    return fragmentConfig;
  }

}
