package org.hotrod.config;

import java.io.File;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;

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

  public void validate(final HotRodConfigTag primaryConfig, final File parentDir,
      final Set<String> alreadyLoadedFileNames, final File parentFile, final DaosTag daosTag)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException {

    log.debug("init");

    // file

    if (SUtils.isEmpty(this.filename)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'file' of tag <" + super.getTagName() + "> cannot be empty. " + "You must specify a file name.");
    }
    this.file = new File(parentDir, this.filename);
    if (!this.file.exists()) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not find fragment file '" + this.file.getAbsolutePath() + "'.");
    }
    if (!this.file.isFile()) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid fragment file '"
          + this.file.getAbsolutePath() + "'. Must be a normal file, not a directory or other special file.");
    }

    log.debug("Will load fragment '" + this.file.getName() + "'");
    this.fragmentConfig = ConfigurationLoader.loadFragment(primaryConfig, this.file, parentFile, alreadyLoadedFileNames,
        daosTag);
    log.debug("Fragment loaded.");
    this.subTags.addAll(this.fragmentConfig.subTags);

  }

  // Getters

  public String getFilename() {
    return filename;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      FragmentTag f = (FragmentTag) fresh;
      return this.filename.equals(f.filename);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      FragmentTag f = (FragmentTag) fresh;
      boolean different = !same(fresh);

      this.filename = f.filename;
      this.file = f.file;
      this.fragmentConfig = f.fragmentConfig;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      FragmentTag f = (FragmentTag) fresh;
      return Compare.same(this.filename, f.filename);
    } catch (ClassCastException e) {
      return false;
    }
  }

}
