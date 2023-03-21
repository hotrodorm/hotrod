package org.hotrod.config;

import java.io.File;
import java.util.LinkedHashSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.utils.FileRegistry;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "fragment")
public class FragmentTag extends AbstractConfigurationTag implements GenerationUnit<FragmentTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(FragmentTag.class);

  // Properties

  private String filename = null;

  private File f;
  private HotRodFragmentConfigTag fragmentConfig;

  // Constructor

  public FragmentTag() {
    super("fragment");
  }

  // JAXB Setters

  @XmlAttribute(name = "file")
  public void setFilename(final String filename) {
    this.filename = filename;
  }

  // Behavior

  public void validate(final HotRodConfigTag primaryConfig, final File parentDir, final FileRegistry fileRegistry,
      final File parentFile, final DaosSpringMyBatisTag daosTag, final DatabaseAdapter adapter,
      final LinkedHashSet<String> facetNames, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException, FacetNotFoundException {

    log.debug("Will load fragment: this.filename=" + this.filename);

    // file

    if (SUtil.isEmpty(this.filename)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'file' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify a (relative) file name.");
    }
    this.f = new File(parentDir, this.filename);
    if (!this.f.exists()) {
      // log.info("Stack: " + LogUtil.renderStack());
      InvalidConfigurationFileException e1 = new InvalidConfigurationFileException(this,
          "Could not find fragment file '" + this.f.getPath() + "'.");
      throw e1;
    }
    if (!this.f.isFile()) {
      throw new InvalidConfigurationFileException(this, "Invalid fragment file '" + this.f.getPath()
          + "'. Must be a normal file, not a directory or other special file.");
    }

    load(primaryConfig, fileRegistry, daosTag, adapter, facetNames, currentCS);

    log.debug("Fragment loaded.");

  }

  public void load(final HotRodConfigTag primaryConfig, final FileRegistry fileRegistry,
      final DaosSpringMyBatisTag daosTag, final DatabaseAdapter adapter, final LinkedHashSet<String> facetNames,
      final CatalogSchema currentCS) throws UncontrolledException, ControlledException, FacetNotFoundException {
    log.debug("@@@ Will load fragment '" + this.f.getName() + "' -- at " + this.getSourceLocation());
    super.clearChildren();
    this.fragmentConfig = ConfigurationLoader.loadFragment(primaryConfig, this.f, fileRegistry, daosTag, this, adapter,
        facetNames, currentCS);
    log.debug("Fragment loaded.");
    super.addChildren(this.fragmentConfig.getSubTags());
  }

  // Getters

  public String getFilename() {
    return filename;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  public File getFile() {
    return this.f;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.filename;
  }

  // Conclude Generation

  public boolean concludeFragmentGeneration() {
    boolean concluded = this.fragmentConfig.concludeFragmentGeneration();
    if (concluded) {
      super.markConcluded();
    }
    return concluded;
  }

  @Override
  public boolean concludeGeneration(FragmentTag cache, DatabaseAdapter adapter) {
    return this.concludeFragmentGeneration();
  }

}
