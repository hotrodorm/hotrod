package org.hotrod.config.dynamicsql;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.ForEachExpression;

@XmlRootElement(name = "foreach")
public class ForEachTag extends DynamicSQLPart {

  // Constants

  // Constructor

  public ForEachTag() {
    super("foreach");
  }

  // Properties

  private String item = null;
  private String index = null;
  private String collectionText = null;
  private String open = null;
  private String separator = null;
  private String close = null;

  private ParameterisableTextPart collection = null;

  private SQLParameter itemParameter = null;
  private SQLParameter indexParameter = null;
  private SQLParameter body = null;

  // JAXB Setters

  @XmlAttribute
  public void setItem(final String item) {
    this.item = item;
  }

  @XmlAttribute
  public void setIndex(final String index) {
    this.index = index;
  }

  @XmlAttribute
  public void setCollection(final String collection) {
    this.collectionText = collection;
  }

  @XmlAttribute
  public void setOpen(final String open) {
    this.open = open;
  }

  @XmlAttribute
  public void setSeparator(final String separator) {
    this.separator = separator;
  }

  @XmlAttribute
  public void setClose(final String close) {
    this.close = close;
  }

  // Getters

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    if (this.item != null) {
      SQLParameter p = new SQLParameter(this.item, tagIdentification);
      if (parameterDefinitions.find(p) != null) {
        throw new InvalidConfigurationFileException("Invalid <foreach> tag in the body of the tag " + tagIdentification
            + ". The 'item' attribute specifies a parameter or variable that is already defined. Try using a different variable name.");
      }
      this.itemParameter = p;
      parameterDefinitions.add(p);
    } else {
      this.itemParameter = null;
    }

    if (this.index != null) {
      SQLParameter p = new SQLParameter(this.index, tagIdentification);
      if (parameterDefinitions.find(p) != null) {
        throw new InvalidConfigurationFileException("Invalid <foreach> tag in the body of the tag " + tagIdentification
            + ". The 'index' attribute specifies a parameter or variable that is already defined. Try using a different variable name.");
      }
      this.indexParameter = p;
      parameterDefinitions.add(p);
    } else {
      this.indexParameter = null;
    }

    this.collection = this.collectionText == null ? null
        : new ParameterisableTextPart(this.collectionText, tagIdentification, parameterDefinitions);

  }

  @Override
  protected void specificBodyValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    for (DynamicSQLPart p : super.parts) {
      try {
        ParameterisableTextPart s = (ParameterisableTextPart) p;
        for (SQLChunk ch : s.chunks) {
          try {
            LiteralTextPart literal = (LiteralTextPart) ch;
            if (!literal.isEmpty()) {
              throw new InvalidConfigurationFileException(
                  "Invalid <foreach> tag included in the tag " + tagIdentification
                      + ". The body of a <foreach> tag must include one variable occurrence in the form #{name}.");
            }
          } catch (ClassCastException e1) {
            try {
              SQLParameter param = (SQLParameter) ch;
              if (this.body != null) {
                throw new InvalidConfigurationFileException("Invalid <foreach> tag included in the tag "
                    + tagIdentification
                    + ". The body of a <foreach> tag must include one variable occurrence in the form #{name}, but found more than one.");
              }
              this.body = param;
            } catch (ClassCastException e2) {
              throw new InvalidConfigurationFileException(
                  "Invalid <foreach> tag included in the tag " + tagIdentification
                      + ". The body of a <foreach> tag must include one variable occurrence in the form #{name}.");
            }
          }
        }
      } catch (ClassCastException e3) {
        throw new InvalidConfigurationFileException("Invalid <choose> tag included in the tag " + tagIdentification
            + ". The body of a <foreach> tag must include one variable occurrence in the form #{name}, but found an object of class '"
            + p.getClass().getName() + "'.");
      }
    }
    if (this.body == null) {
      throw new InvalidConfigurationFileException("Invalid <foreach> tag included in the tag " + tagIdentification
          + ". The body of a <foreach> tag must include one variable occurrence in the form #{name}, but found none.");
    }

  }

  @Override
  protected void postTagValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    if (this.itemParameter != null) {
      parameterDefinitions.remove(this.itemParameter);
    }
    if (this.indexParameter != null) {
      parameterDefinitions.remove(this.indexParameter);
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
        new TagAttribute("item", new LiteralTextPart(this.item)), //
        new TagAttribute("index", new LiteralTextPart(this.index)), //
        new TagAttribute("collection", this.collection), //
        new TagAttribute("open", new LiteralTextPart(this.open)), //
        new TagAttribute("separator", new LiteralTextPart(this.separator)), //
        new TagAttribute("close", new LiteralTextPart(this.close)) //
    };
    return atts;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    String coll = this.collection.renderStatic(parameterRenderer);
    return new ForEachExpression(this.item, this.index, coll, this.open, this.separator, this.close,
        this.body.getName());
  }

}