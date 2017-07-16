package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.TrimExpression;

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
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    this.prefix = this.prefixText == null ? null : new ParameterisableTextPart(this.prefixText, tagIdentification);
    this.prefixOverrides = this.prefixOverridesText == null ? null
        : new ParameterisableTextPart(this.prefixOverridesText, tagIdentification);

    this.suffix = this.suffixText == null ? null : new ParameterisableTextPart(this.suffixText, tagIdentification);
    this.suffixOverrides = this.suffixOverridesText == null ? null
        : new ParameterisableTextPart(this.suffixOverridesText, tagIdentification);
  }

  // Rendering

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
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {

    String prefix = this.prefix.renderTag(parameterRenderer);
    String prefixOverrides = this.prefixOverrides.renderTag(parameterRenderer);
    String suffix = this.suffix.renderTag(parameterRenderer);
    String suffixOverrides = this.suffixOverrides.renderTag(parameterRenderer);

    List<DynamicExpression> exps = new ArrayList<DynamicExpression>();
    for (DynamicSQLPart p : super.parts) {
      exps.add(p.getJavaExpression(parameterRenderer));
    }

    return new TrimExpression(prefix, prefixOverrides, suffix, suffixOverrides, exps.toArray(new DynamicExpression[0]));
  }

}