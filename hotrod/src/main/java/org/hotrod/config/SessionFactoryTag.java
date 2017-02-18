package org.hotrod.config;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class SessionFactoryTag {

  private static final String TAG_NAME = "session-factory";

  private static final String ATT_NAME = "singleton-full-class-name";

  private String singletonFullClassName = null;

  // Validation

  public void validate() throws InvalidConfigurationFileException {

    // singleton-full-class-name

    if (SUtils.isEmpty(this.singletonFullClassName)) {
      throw new InvalidConfigurationFileException("Attribute '" + ATT_NAME
          + "' of tag <" + TAG_NAME + "> cannot be empty. "
          + "Must specify the singleton "
          + "that provides the SessionFactory object.");
    }

  }

  // Setters (digester)

  public void setSingletonFullClassName(final String singletonFullClassName) {
    this.singletonFullClassName = singletonFullClassName;
  }

  // Getters

  public String getSingletonFullClassName() {
    return singletonFullClassName;
  }

  public String getSessionFactoryGetter() {
    return this.singletonFullClassName + ".getInstance().getSqlSessionFactory()";
  }

}
