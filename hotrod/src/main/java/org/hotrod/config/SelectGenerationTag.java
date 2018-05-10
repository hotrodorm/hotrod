package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "select-generation")
public class SelectGenerationTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(SelectGenerationTag.class);

  private static final String VIEW_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";
  private static final String ATT_NAME = "temp-view-base-name";

  // Properties

  private String tempViewBaseName = null;
  private int folio = 0;

  // Constructor

  public SelectGenerationTag() {
    super("select-generation");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "temp-view-base-name")
  public void setTempViewBaseName(final String tempViewBaseName) {
    this.tempViewBaseName = tempViewBaseName;
  }

  // Behavior

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // temp-view-base-name

    if (SUtils.isEmpty(this.tempViewBaseName)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName() + "> cannot be empty", //
          "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify a temporary view name, " + "a name that is NOT used by any existing table, "
              + "view or any other database object on this database. "
              + "A view with this name may be created and dropped " + "several times during the DAO generation.");
    }
    if (!this.tempViewBaseName.matches(VIEW_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(this, //
          "Invalid attribute '" + ATT_NAME + "': when specified it must start with a letter, "
              + "and continue with one or more " + "letters, digits, or undersore characters", //
          "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName()
              + "> must be a valid view name. Specified value is '" + this.tempViewBaseName
              + "' but must start with a letter, " + "and continue with one or more "
              + "letters, digits, or undersore characters.");
    }

  }

  // Getters

  public synchronized String getNextTempViewName() {
    return this.tempViewBaseName + (this.folio++);
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SelectGenerationTag f = (SelectGenerationTag) fresh;
      boolean different = !same(fresh);

      this.tempViewBaseName = f.tempViewBaseName;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SelectGenerationTag f = (SelectGenerationTag) fresh;
      return Compare.same(this.tempViewBaseName, f.tempViewBaseName);
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
