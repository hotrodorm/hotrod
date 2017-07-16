package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.ForEachExpression;

@XmlRootElement(name = "foreach")
public class ForEachTag extends DynamicSQLPart {

  // Constructor

  public ForEachTag() {
    super("foreach");
  }

  // Properties

  private String itemText = null;
  private String indexText = null;
  private String collectionText = null;
  private String openText = null;
  private String separatorText = null;
  private String closeText = null;

  private ParameterisableTextPart item = null;
  private ParameterisableTextPart index = null;
  private ParameterisableTextPart collection = null;
  private ParameterisableTextPart open = null;
  private ParameterisableTextPart separator = null;
  private ParameterisableTextPart close = null;

  // JAXB Setters

  @XmlAttribute
  public void setItem(final String item) {
    this.itemText = item;
  }

  @XmlAttribute
  public void setIndex(final String index) {
    this.indexText = index;
  }

  @XmlAttribute
  public void setCollection(final String collection) {
    this.collectionText = collection;
  }

  @XmlAttribute
  public void setOpen(final String open) {
    this.openText = open;
  }

  @XmlAttribute
  public void setSeparator(final String separator) {
    this.separatorText = separator;
  }

  @XmlAttribute
  public void setClose(final String close) {
    this.closeText = close;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    this.item = this.itemText == null ? null : new ParameterisableTextPart(this.itemText, tagIdentification);
    this.index = this.indexText == null ? null : new ParameterisableTextPart(this.indexText, tagIdentification);
    this.collection = this.collectionText == null ? null
        : new ParameterisableTextPart(this.collectionText, tagIdentification);

    this.open = this.openText == null ? null : new ParameterisableTextPart(this.openText, tagIdentification);
    this.separator = this.separatorText == null ? null
        : new ParameterisableTextPart(this.separatorText, tagIdentification);
    this.close = this.closeText == null ? null : new ParameterisableTextPart(this.closeText, tagIdentification);
  }

  // Rendering

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = { //
        new TagAttribute("item", this.item), //
        new TagAttribute("index", this.index), //
        new TagAttribute("collection", this.collection), //
        new TagAttribute("open", this.open), //
        new TagAttribute("separator", this.separator), //
        new TagAttribute("close", this.close) //
    };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {

    String it = this.item.renderTag(parameterRenderer);
    String in = this.index.renderTag(parameterRenderer);
    String co = this.collection.renderTag(parameterRenderer);

    String op = this.open.renderTag(parameterRenderer);
    String se = this.separator.renderTag(parameterRenderer);
    String cl = this.close.renderTag(parameterRenderer);

    return new ForEachExpression(it, in, co, op, se, cl);
  }

}