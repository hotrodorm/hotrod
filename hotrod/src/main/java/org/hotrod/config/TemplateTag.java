package org.hotrod.config;

import java.io.File;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class TemplateTag {

  private static final String TAG_NAME = "mybatis-configuration-template";

  private String sFile = null;

  private File file;

  // Validation

  public void validate(final File basedir)
      throws InvalidConfigurationFileException {

    // file

    if (SUtils.isEmpty(this.sFile)) {
      throw new InvalidConfigurationFileException(
          "MyBatis configuration template not found. "
              + "Attribute 'file' of tag <" + TAG_NAME + "> cannot be empty. "
              + "Must specify the MyBatis configuration template file.");
    }
    this.file = new File(basedir, this.sFile);
    if (!this.file.exists()) {
      throw new InvalidConfigurationFileException(
          "MyBatis configuration template not found. "
              + "Attribute 'file' of tag <" + TAG_NAME + "> with value '"
              + this.sFile + "' points to a non-existing file.");
    }
    if (!this.file.isFile()) {
      throw new InvalidConfigurationFileException("Attribute 'file' of tag <"
          + TAG_NAME + "> with value '" + this.sFile + "' is not a file.");
    }

  }

  // Setters (digester)

  public void setTemplateFile(final String file) {
    this.sFile = file;
  }

  // Getters

  public File getFile() {
    return file;
  }

}
