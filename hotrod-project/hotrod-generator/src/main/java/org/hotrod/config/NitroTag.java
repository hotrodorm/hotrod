package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "nitro")
public class NitroTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(NitroTag.class);

  private static final String DEFAULT_DAO_PREFIX = "";
  private static final String DEFAULT_DAO_SUFFIX = "DAO";
  private static final String DEFAULT_VO_PREFIX = "";
  private static final String DEFAULT_VO_SUFFIX = "";
  private static final String DEFAULT_ABSTRACT_VO_PREFIX = "Abstract";
  private static final String DEFAULT_ABSTRACT_VO_SUFFIX = "";

  private static final String PREFIX_SUFFIX_PATTERN = "[A-Za-z0-9_]*";

  // Properties

  private String daoPrefix = null;
  private String daoSuffix = null;
  private String voPrefix = null;
  private String voSuffix = null;
  private String abstractVoPrefix = null;
  private String abstractVoSuffix = null;

  // Constructor

  public NitroTag() {
    super("nitro");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "dao-prefix")
  public void setDaoPrefix(final String daoPrefix) {
    this.daoPrefix = daoPrefix;
  }

  @XmlAttribute(name = "dao-suffix")
  public void setDaoSuffix(final String daoSuffix) {
    this.daoSuffix = daoSuffix;
  }

  @XmlAttribute(name = "vo-prefix")
  public void setVoPrefix(final String voPrefix) {
    this.voPrefix = voPrefix;
  }

  @XmlAttribute(name = "vo-suffix")
  public void setVoSuffix(final String voSuffix) {
    this.voSuffix = voSuffix;
  }

  @XmlAttribute(name = "abstract-vo-prefix")
  public void setAbstractVoPrefix(final String abstractVoPrefix) {
    this.abstractVoPrefix = abstractVoPrefix;
  }

  @XmlAttribute(name = "abstract-vo-suffix")
  public void setAbstractVoSuffix(final String abstractVoSuffix) {
    this.abstractVoSuffix = abstractVoSuffix;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // dao-prefix

    if (this.daoPrefix == null) {
      this.daoPrefix = DEFAULT_DAO_PREFIX;
    } else {
      if (!this.daoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid dao-prefix value '" + this.daoPrefix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // dao-suffix

    if (this.daoSuffix == null) {
      this.daoSuffix = DEFAULT_DAO_SUFFIX;
    } else {
      if (!this.daoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid dao-suffix value '" + this.daoSuffix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // vo-prefix

    if (this.voPrefix == null) {
      this.voPrefix = DEFAULT_VO_PREFIX;
    } else {
      if (!this.voPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid vo-prefix value '" + this.voPrefix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // vo-suffix

    if (this.voSuffix == null) {
      this.voSuffix = DEFAULT_VO_SUFFIX;
    } else {
      if (!this.voSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid vo-suffix value '" + this.daoSuffix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // abstract-vo-prefix

    if (this.abstractVoPrefix == null) {
      this.abstractVoPrefix = DEFAULT_ABSTRACT_VO_PREFIX;
    } else {
      if (!this.abstractVoPrefix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid abstract-vo-prefix value '" + this.voPrefix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

    // abstract-vo-suffix

    if (this.abstractVoSuffix == null) {
      this.abstractVoSuffix = DEFAULT_ABSTRACT_VO_SUFFIX;
    } else {
      if (!this.abstractVoSuffix.matches(PREFIX_SUFFIX_PATTERN)) {
        throw new InvalidConfigurationFileException(this, "Invalid abstract-vo-suffix value '" + this.daoSuffix
            + "': when specified, it can only include one or more letters, digits, or underscores");
      }
    }

  }

  // Getters

  public String getDaoPrefix() {
    return daoPrefix;
  }

  public String getDaoSuffix() {
    return daoSuffix;
  }

  public String getVoPrefix() {
    return voPrefix;
  }

  public String getVoSuffix() {
    return voSuffix;
  }

  public String getAbstractVoPrefix() {
    return abstractVoPrefix;
  }

  public String getAbstractVoSuffix() {
    return abstractVoSuffix;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      NitroTag f = (NitroTag) fresh;
      boolean different = !same(fresh);

      this.daoPrefix = f.daoPrefix;
      this.daoSuffix = f.daoSuffix;
      this.voPrefix = f.voPrefix;
      this.voSuffix = f.voSuffix;
      this.abstractVoPrefix = f.abstractVoPrefix;
      this.abstractVoSuffix = f.abstractVoSuffix;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      NitroTag f = (NitroTag) fresh;
      return //
      Compare.same(this.daoPrefix, f.daoPrefix) && //
          Compare.same(this.daoSuffix, f.daoSuffix) && //
          Compare.same(this.voPrefix, f.voPrefix) && //
          Compare.same(this.voSuffix, f.voSuffix) && //
          Compare.same(this.abstractVoPrefix, f.abstractVoPrefix) && //
          Compare.same(this.abstractVoSuffix, f.abstractVoSuffix);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
