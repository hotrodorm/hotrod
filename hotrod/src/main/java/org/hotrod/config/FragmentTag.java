package org.hotrod.config;

import java.io.File;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

@XmlRootElement(name = "fragment")
public class FragmentTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(FragmentTag.class);

  // Properties

  private String filename = null;

  private File file;
  private HotRodFragmentConfigTag fragmentConfig;

  // Constructor

  public FragmentTag() {
    super("fragment");
  }

  // JAXB Setters

  @XmlAttribute
  public void setFile(final String filename) {
    this.filename = filename;
  }

  // Behavior

  public void validate(final HotRodConfigTag primaryConfig, final File baseDir,
      final Set<String> alreadyLoadedFileNames, final File parentFile, final DaosTag daosTag)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException {

    log.debug("init");

    // file

    if (SUtils.isEmpty(this.filename)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'file' of tag <" + super.getTagName() + "> cannot be empty. " + "You must specify a file name.");
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

    log.debug("Will load fragment '" + this.file.getName() + "'");
    this.fragmentConfig = ConfigurationLoader.loadFragment(primaryConfig, this.file, parentFile, alreadyLoadedFileNames,
        daosTag);
    log.debug("Fragment loaded.");

  }

  // Getters

  public String getFilename() {
    return filename;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

}
