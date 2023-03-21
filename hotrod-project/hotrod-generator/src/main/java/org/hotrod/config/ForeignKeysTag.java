package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "foreign-keys")
public class ForeignKeysTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ForeignKeysTag.class);

  static final String TAG_NAME = "foreign-keys";

  // Properties

  private List<ForeignKeyTag> fks = new ArrayList<ForeignKeyTag>();

  // Constructor

  public ForeignKeysTag() {
    super("foreign-keys");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement(name = "foreign-key")
  public void setForeignKey(final ForeignKeyTag foreignKeyTag) {
    this.fks.add(foreignKeyTag);
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

  }

  // Getters

  public List<ForeignKeyTag> getFKs() {
    return fks;
  }

  @Override
  public String getInternalCaption() {
    return TAG_NAME;
  }

}
