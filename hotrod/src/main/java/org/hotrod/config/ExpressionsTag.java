package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;

@XmlRootElement(name = "expressions")
public class ExpressionsTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ExpressionsTag.class);

  // Properties

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
  })
  private List<Object> content = new ArrayList<Object>();

  private String expressions = "";

  // Constructor

  public ExpressionsTag() {
    super("expressions");
  }

  // JAXB Setters

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: columns and expressions

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (!SUtils.isEmpty(s)) {
          if (!this.expressions.isEmpty()) {
            this.expressions = this.expressions + " ";
          }
          this.expressions = this.expressions + s;
        }
      } catch (ClassCastException e1) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
            + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
      }
    }

    // expressions in body

    if (SUtils.isEmpty(this.expressions)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid empty <" + super.getTagName()
          + "> tag. " + "When specified, it must include a comma-separated list of expressions.");
    }

  }

  // Getters

  public String getExpressions() {
    return expressions;
  }

}
