package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.TrimExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "trim")
public class TrimTag extends DynamicSQLPart {

  // Constructor

  public TrimTag() {
    super("trim");
  }

  // Properties

  private String prefixText = null;
  private String prefixOverridesText = null;
  private String suffixText = null;
  private String suffixOverridesText = null;

  private ParameterisableTextPart prefix = null;
  private ParameterisableTextPart prefixOverrides = null;
  private ParameterisableTextPart suffix = null;
  private ParameterisableTextPart suffixOverrides = null;

  // JAXB Setters

  @XmlAttribute
  public void setPrefix(final String prefix) {
    this.prefixText = prefix;
  }

  @XmlAttribute
  public void setPrefixOverrides(final String prefixOverrides) {
    this.prefixOverridesText = prefixOverrides;
  }

  @XmlAttribute
  public void setSuffix(final String suffix) {
    this.suffixText = suffix;
  }

  @XmlAttribute
  public void setSuffixOverrides(final String suffixOverrides) {
    this.suffixOverridesText = suffixOverrides;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    this.prefix = this.prefixText == null ? null
        : new ParameterisableTextPart(this.prefixText, tagIdentification, parameterDefinitions);
    this.prefixOverrides = this.prefixOverridesText == null ? null
        : new ParameterisableTextPart(this.prefixOverridesText, tagIdentification, parameterDefinitions);

    this.suffix = this.suffixText == null ? null
        : new ParameterisableTextPart(this.suffixText, tagIdentification, parameterDefinitions);
    this.suffixOverrides = this.suffixOverridesText == null ? null
        : new ParameterisableTextPart(this.suffixOverridesText, tagIdentification, parameterDefinitions);
  }

  @Override
  protected void specificBodyValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    for (Iterator<DynamicSQLPart> it = super.parts.iterator(); it.hasNext();) {
      DynamicSQLPart p = it.next();
      try {
        ParameterisableTextPart text = (ParameterisableTextPart) p;
        if (!text.isEmpty()) {
          throw new InvalidConfigurationFileException("Invalid <trim> tag included in the tag " + tagIdentification
              + ". A <trim> tag can only include other tags, but no free text content in it.");
        }
        it.remove();
      } catch (ClassCastException e3) {
        // Nothing to do
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

      String prefix = this.prefix == null ? null : this.prefix.renderStatic(parameterRenderer);
      String prefixOverrides = this.prefixOverrides == null ? null
          : this.prefixOverrides.renderStatic(parameterRenderer);
      String suffix = this.suffix == null ? null : this.suffix.renderStatic(parameterRenderer);
      String suffixOverrides = this.suffixOverrides == null ? null
          : this.suffixOverrides.renderStatic(parameterRenderer);

      List<DynamicExpression> exps = new ArrayList<DynamicExpression>();
      for (DynamicSQLPart p : super.parts) {
        exps.add(p.getJavaExpression(parameterRenderer));
      }

      return new TrimExpression(prefix, prefixOverrides, suffix, suffixOverrides,
          exps.toArray(new DynamicExpression[0]));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for tag <trim> on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

}