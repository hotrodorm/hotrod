package org.hotrod.config.dynamicsql;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.TrimExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "trim")
public class TrimTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constructor

  public TrimTag() {
    super("trim");
  }

  // Properties

  private String prefix = null;
  private String prefixOverrides = null;
  private String suffix = null;
  private String suffixOverrides = null;

  // JAXB Setters

  @XmlAttribute
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  @XmlAttribute
  public void setPrefixOverrides(final String prefixOverrides) {
    this.prefixOverrides = prefixOverrides;
  }

  @XmlAttribute
  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

  @XmlAttribute
  public void setSuffixOverrides(final String suffixOverrides) {
    this.suffixOverrides = suffixOverrides;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // Nothing to validate
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    for (Iterator<DynamicSQLPart> it = super.parts.iterator(); it.hasNext();) {
      DynamicSQLPart p = it.next();
      try {
        ParameterisableTextPart text = (ParameterisableTextPart) p;
        if (!text.isEmpty()) {
          throw new InvalidConfigurationFileException(this, //
              "A <trim> tag can only include other tags, but no free text content in it", //
              "Invalid <trim> tag. " + "A <trim> tag can only include other tags, but no free text content in it.");
        }
        // it.remove();
      } catch (ClassCastException e3) {
        // Nothing to do for other kinds of part.
      }
    }

  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return true;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = { //
        new TagAttribute("prefix", this.prefix), //
        new TagAttribute("prefixOverrides", this.prefixOverrides), //
        new TagAttribute("suffix", this.suffix), //
        new TagAttribute("suffixOverrides", this.suffixOverrides) //
    };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new TrimExpression(this.prefix, this.prefixOverrides, this.suffix, this.suffixOverrides,
          toArray(this.parts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for tag <trim>: " + e.getMessage());
    }

  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    try {
      TrimTag f = (TrimTag) fresh;
      return //
      Compare.same(this.prefix, f.prefix) && //
          Compare.same(this.prefixOverrides, f.prefixOverrides) && //
          Compare.same(this.suffix, f.suffix) && //
          Compare.same(this.suffixOverrides, f.suffixOverrides);
    } catch (ClassCastException e) {
      return false;
    }
  }

}