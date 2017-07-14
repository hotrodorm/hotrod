package org.hotrod.config.dynamicsql;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "trim")
public class TrimTag extends DynamicSQLPart {

  // Constructor

  public TrimTag() {
    super("trim");
  }

  // Properties

  private String prefix = null;
  private String prefixOverrides = null;
  private String suffix = null;
  private String suffixOverrides = null;

  // Getters & Setters

  public String getPrefix() {
    return prefix;
  }

  @XmlAttribute
  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  public String getPrefixOverrides() {
    return prefixOverrides;
  }

  @XmlAttribute
  public void setPrefixOverrides(final String prefixOverrides) {
    this.prefixOverrides = prefixOverrides;
  }

  public String getSuffix() {
    return suffix;
  }

  @XmlAttribute
  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

  public String getSuffixOverrides() {
    return suffixOverrides;
  }

  @XmlAttribute
  public void setSuffixOverrides(final String suffixOverrides) {
    this.suffixOverrides = suffixOverrides;
  }

  // Behavior

  @Override
  public void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    // No validation necessary
  }

  @Override
  public List<SQLParameter> getParameters() {
    return null;
  }

  // Rendering

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return super.renderTag(parameterRenderer, //
        new TagAttribute("prefix", this.prefix), //
        new TagAttribute("prefixOverrides", this.prefixOverrides), //
        new TagAttribute("suffix", this.suffix), //
        new TagAttribute("suffixOverrides", this.suffixOverrides) //
    );
  }

}