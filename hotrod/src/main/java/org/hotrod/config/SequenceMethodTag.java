package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

@XmlRootElement(name = "sequence")
public class SequenceMethodTag extends AbstractMethodTag<SequenceMethodTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(SequenceMethodTag.class);

  static final String TAG_NAME = "sequence";

  private static final String METHOD_PREFIX = "selectSequence";

  // Properties

  private String name = null;

  // Constructor

  public SequenceMethodTag() {
    super("sequence");
    log.debug("init");
  }

  // Duplicate

  @Override
  public SequenceMethodTag duplicate() {
    SequenceMethodTag d = new SequenceMethodTag();

    d.copyCommon(this);

    d.name = this.name;

    return d;
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "method")
  public void setMethod(final String method) {
    this.method = method;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. " + "You must specify a sequence name.");
    }

    // method

    if (this.method == null) {
      this.method = METHOD_PREFIX + this.getIdentifier().getJavaClassIdentifier();
    } else {
      if (!this.method.matches(Patterns.VALID_JAVA_METHOD)) {
        throw new InvalidConfigurationFileException(this, //
            "Attribute 'method' of tag <" + TAG_NAME + "> specifies '" + this.method
                + "' but must specify a valid java method name. "
                + "Valid method names must start with a lowercase letter, "
                + "and continue with letters, digits, dollarsign, and/or underscores", //
            "Attribute 'method' of tag <" + TAG_NAME + "> specifies '" + this.method
                + "' but must specify a valid java method name. "
                + "Valid method names must start with a lowercase letter, "
                + "and continue with letters, digits, dollarsign, and/or underscores.");
      }
    }

  }

  // Getters

  public String getName() {
    return name;
  }

  public String getMethod() {
    return method;
  }

  public Identifier getIdentifier() {
    return new DbIdentifier(this.name);
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      SequenceMethodTag f = (SequenceMethodTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SequenceMethodTag f = (SequenceMethodTag) fresh;
      boolean different = !same(fresh);

      this.method = f.method;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SequenceMethodTag f = (SequenceMethodTag) fresh;
      return //
      Compare.same(this.name, f.name) && //
          Compare.same(this.method, f.method);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
