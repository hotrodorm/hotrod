package org.hotrod.config;

import java.util.logging.Logger;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.hotrod.utils.SUtil;

public abstract class AbstractMethodTag<M extends AbstractMethodTag<M>> extends AbstractConfigurationTag {

  private static final Logger log = Logger.getLogger(AbstractMethodTag.class.getName());

  // Properties

  protected String method = null;
  protected String sSQLInjectionEnabled = null;
  protected Id id = null;

  protected boolean sqlInjectionEnabled;

  // Constructor

  protected AbstractMethodTag(final String tagName) {
    super(tagName);
  }

  protected void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // method

    if (this.method == null) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'method' of tag <" + getTagName() + "> cannot be empty. " + "Must specify a unique name.");
    }
    if (SUtil.isEmpty(this.method)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'method' of tag <" + getTagName()
          + "> cannot be empty. " + "A unique Java method name was expected for the DAO class.");
    }
    if (!this.method.matches(Patterns.VALID_JAVA_METHOD)) {
      throw new InvalidConfigurationFileException(this,
          "Invalid method name '" + this.method + "' on tag <" + super.getTagName()
              + ">. A Java method name must start with a lower case letter, "
              + "and continue with letters, digits, and/or underscores.");
    }

    try {
      this.id = Id.fromJavaMember(this.method);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid Java method name '" + this.method + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // sql-injection-enabled

    if (this.sSQLInjectionEnabled == null) {
      this.sqlInjectionEnabled = false;
    } else if ("true".equals(this.sSQLInjectionEnabled)) {
      this.sqlInjectionEnabled = true;
    } else if ("false".equals(this.sSQLInjectionEnabled)) {
      this.sqlInjectionEnabled = false;
    } else {
      throw new InvalidConfigurationFileException(this, //
          "Invalid valued '" + this.sSQLInjectionEnabled
              + "' for the attribute 'sql-injection-enabled' in the <select> tag; "
              + "either use the value 'true' or 'false'.");
    }

  }

  // Getters

  public abstract String getMethod();

  public Id getId() {
    return id;
  }

  public boolean isSqlInjectionEnabled() {
    return sqlInjectionEnabled;
  }

}
