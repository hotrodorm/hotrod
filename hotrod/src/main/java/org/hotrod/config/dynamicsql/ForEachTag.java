package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "foreach")
public class ForEachTag extends DynamicSQLPart {

  // Constructor

  public ForEachTag() {
    super("foreach");
  }

  // Properties

  private String item = null;
  private String index = null;
  private String collection = null;
  private String open = null;
  private String separator = null;
  private String close = null;

  // Getters & Setters

  public String getItem() {
    return item;
  }

  @XmlAttribute
  public void setItem(final String item) {
    this.item = item;
  }

  public String getIndex() {
    return index;
  }

  @XmlAttribute
  public void setIndex(final String index) {
    this.index = index;
  }

  public String getCollection() {
    return collection;
  }

  @XmlAttribute
  public void setCollection(final String collection) {
    this.collection = collection;
  }

  public String getOpen() {
    return open;
  }

  @XmlAttribute
  public void setOpen(final String open) {
    this.open = open;
  }

  public String getSeparator() {
    return separator;
  }

  @XmlAttribute
  public void setSeparator(final String separator) {
    this.separator = separator;
  }

  public String getClose() {
    return close;
  }

  @XmlAttribute
  public void setClose(final String close) {
    this.close = close;
  }

  // Rendering

  @Override
  public String render() {
    return super.renderTag( //
        new TagAttribute("item", this.item), //
        new TagAttribute("index", this.index), //
        new TagAttribute("collection", this.collection), //
        new TagAttribute("open", this.open), //
        new TagAttribute("separator", this.separator), //
        new TagAttribute("close", this.close) //
    );
  }

}