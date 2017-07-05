package xsdtests.case10.dynamicsql.tags;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case10.configuration.TagAttribute;

@XmlRootElement(name = "trim")
public class TrimTag extends DynamicSQLTag {

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

  // Rendering

  @Override
  public String render() {
    return super.renderTag( //
        new TagAttribute("prefix", this.prefix), //
        new TagAttribute("prefixOverrides", this.prefixOverrides), //
        new TagAttribute("suffix", this.suffix), //
        new TagAttribute("suffixOverrides", this.suffixOverrides) //
    );
  }

}