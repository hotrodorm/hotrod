package org.hotrod.config;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "select-generation")
public class SelectGenerationTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SelectGenerationTag.class);

  public static final SelectStrategy DEFAULT_STRATEGY = SelectStrategy.RESULT_SET;
  public static final String DEFAULT_TEMP_VIEW_NAME = "hotrodtempview";

  private static final String VIEW_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9]*";
  private static final String ATT_NAME = "temp-view-base-name";

  public enum SelectStrategy {
    CREATE_VIEW("create-view"), //
    RESULT_SET("result-set");

    private String caption;

    private SelectStrategy(final String caption) {
      this.caption = caption;
    }

    public String getCaption() {
      return caption;
    }

    public static SelectStrategy parse(final String txt) {
      for (SelectStrategy s : SelectStrategy.values()) {
        if (s.caption.equals(txt)) {
          return s;
        }
      }
      return null;
    }

  }

  // Properties

  private String sStrategy = null;
  private String tempViewBaseName = null;

  private SelectStrategy strategy = null;

  private int folio = 0;

  // Constructor

  public SelectGenerationTag() {
    super("select-generation");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "strategy")
  public void setSStrategy(final String strategy) {
    log.debug("strategy=" + strategy);
    this.sStrategy = strategy;
  }

  @XmlAttribute(name = "temp-view-base-name")
  public void setTempViewBaseName(final String tempViewBaseName) {
    log.debug("tempViewBaseName=" + tempViewBaseName);
    this.tempViewBaseName = tempViewBaseName;
  }

  // Behavior

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // strategy

    log.debug("this.sStrategy=" + this.sStrategy);
    if (this.sStrategy == null) {
      this.strategy = DEFAULT_STRATEGY;
    } else {
      this.strategy = SelectStrategy.parse(this.sStrategy);
      if (this.strategy == null) {
        throw new InvalidConfigurationFileException(this,
            "Invalid value '" + this.sStrategy + "' on 'strategy' attribute of tag <" + super.getTagName()
                + ">. Valid values are: "
                + Stream.of(SelectStrategy.values()).map(s -> s.getCaption()).collect(Collectors.joining(", ")));
      }
    }
    log.debug("this.strategy=" + this.strategy);

    // temp-view-base-name

    if (this.tempViewBaseName == null) {
      this.tempViewBaseName = DEFAULT_TEMP_VIEW_NAME;
    } else {
      if (SUtil.isEmpty(this.tempViewBaseName)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName() + "> cannot be empty. "
                + "Must specify a temporary view name, " + "a name that is NOT used by any existing table, "
                + "view or any other database object on this database. "
                + "A view with this name may be created and dropped " + "several times during the DAO generation.");
      }
      if (!this.tempViewBaseName.matches(VIEW_NAME_PATTERN)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute '" + ATT_NAME + "' of tag <" + super.getTagName()
                + "> must be a valid view name. Specified value is '" + this.tempViewBaseName
                + "' but must start with a letter, " + "and continue with one or more " + "letters, or digits.");
      }
    }

  }

  // Getters

  public synchronized String getNextTempViewName() {
    return this.tempViewBaseName + (this.folio++);
  }

  public String getsStrategy() {
    return sStrategy;
  }

  public SelectStrategy getStrategy() {
    return this.strategy;
  }

  public String getTempViewBaseName() {
    return this.tempViewBaseName;
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

      this.strategy = f.strategy;
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
      return Compare.same(this.strategy, f.strategy) && Compare.same(this.tempViewBaseName, f.tempViewBaseName);
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
