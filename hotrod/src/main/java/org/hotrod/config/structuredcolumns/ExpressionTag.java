package org.hotrod.config.structuredcolumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.util.SUtils;

@XmlRootElement(name = "expression")
public class ExpressionTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ExpressionTag.class);

  // Properties

  private String property = null;
  private String className = null;
  private String converter = null;

  private boolean isId = false;

  private ConverterTag converterTag;
  private StructuredColumnMetadata metadata;
  private String tempAlias = null;
  private String namespacedAlias;

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  private List<Object> content = new ArrayList<Object>();

  private String body;

  // Constructor

  public ExpressionTag() {
    super("expression");
  }

  // JAXB Setters

  @XmlAttribute(name = "property")
  public void setProperty(final String property) {
    this.property = property;
  }

  @XmlAttribute(name = "class")
  public void setClassName(final String className) {
    this.className = className;
  }

  @XmlAttribute(name = "converter")
  public void setConverter(final String converter) {
    this.converter = converter;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult, final Set<String> ids)
      throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: body

    this.body = null;
    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (!SUtils.isEmpty(s)) {
          if (this.body == null) {
            this.body = s;
          } else {
            this.body = this.body + " " + s;
          }
        }
      } catch (ClassCastException e1) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
            + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
      }
    }

    // body

    if (this.body == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Missing SQL expression in <" + super.getTagName() + "> tag. " + "An <" + super.getTagName()
              + "> tag must include a body with the SQL expression in it.");
    }
    cleanBody();
    if (SUtils.isEmpty(this.body)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid empty <" + super.getTagName() + "> tag. " + "An <" + super.getTagName()
              + "> tag must include a non-empty body with the SQL expression in it.");
    }

    // property

    if (this.property == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'property' attribute in <"
          + super.getTagName() + "> tag. " + "An <" + super.getTagName() + "> must specify the 'property' attribute.");
    }
    if (!this.property.matches(Patterns.VALID_JAVA_PROPERTY)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid property name '" + this.property + "' in the <" + super.getTagName()
              + "> tag. A Java property name must start with a lower case letter, "
              + "and continue with letters, digits, and/or underscores.");
    }
    if (ids != null) {
      this.isId = ids.remove(this.property);
    }

    // class

    if (this.className != null) {
      if (!this.className.matches(Patterns.VALID_JAVA_TYPE)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid class name '" + this.className + "' in the <" + super.getTagName()
                + "> tag. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // converter

    if (this.converter != null) {
      if (this.className != null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid attribute 'class' of tag <" + super.getTagName() + ">: "
                + "the 'class' and 'converter' attributes are mutually exclusive, so only one of them can be specified in an <"
                + super.getTagName() + "> definition.");
      }
      if (SUtils.isEmpty(this.converter)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'converter' of tag <"
            + super.getTagName() + "> cannot be empty. " + "Must specify a valid converter name.");
      }
      this.converterTag = config.getConverterTagByName(this.converter);
      if (this.converterTag == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Converter '" + this.converter + "' not found.");
      }
    } else {
      this.converterTag = null;
    }

  }

  private void cleanBody() {
    String b = this.body.trim();
    while (b.startsWith(",") || b.endsWith(",")) {
      if (b.startsWith(",")) {
        b = b.substring(1).trim();
      }
      if (b.endsWith(",")) {
        b = b.substring(0, b.length() - 1).trim();
      }
    }
    this.body = b;
  }

  // TODO: remove this section

  // Meta data gathering

  // @Override
  // public void gatherMetadataPhase2(final Connection conn2)
  // throws InvalidSQLException, UncontrolledException,
  // UnresolvableDataTypeException, ControlledException {
  // List<StructuredColumnMetadata> exps =
  // this.columnsRetriever.retrieve(conn2);
  //
  // log.debug("expressions=" + exps.size());
  //
  // // Check column tags actually have related expressions
  //
  // for (ColumnTag t : this.columns) {
  // StructuredColumnMetadata exp = findExpression(t.getName(), exps);
  // if (exp == null) {
  // throw new ControlledException("Invalid <" + new ColumnTag().getTagName() +
  // "> tag with name '" + t.getName()
  // + "' at " + t.getSourceLocation().render() + ".\n" + "There's no expression
  // with the name '" + t.getName()
  // + "'.");
  // }
  // }
  //
  // // Apply column tags to meta data
  //
  // this.columnsMetadata = new ArrayList<StructuredColumnMetadata>();
  // for (StructuredColumnMetadata exp : exps) {
  // ColumnTag t = findColumnTag(exp.getColumnName());
  //
  // if (t == null) {
  // log.debug("[exp] " + exp.getColumnName() + " t=null");
  // } else {
  // log.debug("[exp] " + exp.getColumnName() + " t=" + t.getName() + " (" +
  // t.getJavaType() + ")");
  // }
  //
  // if (t == null) {
  // this.columnsMetadata.add(exp);
  // } else {
  // StructuredColumnMetadata scm = StructuredColumnMetadata.applyColumnTag(exp,
  // t);
  // this.columnsMetadata.add(scm);
  // }
  // }
  //
  // // apply new namespaced alias to the expressions
  //
  // for (Expression e : this.expressions) {
  // StructuredColumnMetadata m = findColumn(e.getOriginalName());
  // if (m == null) {
  // throw new ControlledException(
  // "Invalid <" + super.getTagName() + "> tag. Could not find column '" +
  // e.getOriginalName() + "'.");
  // }
  // e.setNewNamespacedAlias(m.getColumnAlias());
  // m.setFormula(e.getFormula());
  // }
  //
  // }

  // private StructuredColumnMetadata findColumn(final String originalName) {
  // for (StructuredColumnMetadata m : this.columnsMetadata) {
  // if (this.generator.getAdapter().isColumnIdentifier(m.getColumnName(),
  // originalName)) {
  // return m;
  // }
  // }
  // return null;
  // }
  //
  // private StructuredColumnMetadata findExpression(final String configName,
  // final List<StructuredColumnMetadata> exps) {
  // for (StructuredColumnMetadata m : exps) {
  // if (this.generator.getAdapter().isColumnIdentifier(m.getColumnName(),
  // configName)) {
  // return m;
  // }
  // }
  // return null;
  // }
  //
  // private ColumnTag findColumnTag(final String expressionName) {
  // for (ColumnTag t : this.columns) {
  // if (this.generator.getAdapter().isColumnIdentifier(expressionName,
  // t.getName())) {
  // return t;
  // }
  // }
  // return null;
  // }

  // Rendering

  // public List<String> gelAliasedSQLColumns() {
  // List<String> columns = new ArrayList<String>();
  // for (Expression e : this.expressions) {
  // columns.add(e.getFormula() + " as " + e.getNewNamespacedAlias());
  // }
  // return columns;
  // }

  // toString

  // public String toString() {
  // return "{ExpressionsTag: count=" + this.expressions.size() + "}";
  // }

  // Setters

  public void setTempAlias(final String tempAlias) {
    this.tempAlias = tempAlias;
  }

  public void setMetadata(final StructuredColumnMetadata metadata) {
    this.metadata = metadata;
  }

  // Getters

  public StructuredColumnMetadata getMetadata() {
    return this.metadata;
  }

  public String getProperty() {
    return property;
  }

  public String getClassName() {
    return className;
  }

  public String getConverter() {
    return converter;
  }

  ConverterTag getConverterTag() {
    return converterTag;
  }

  public String getBody() {
    return body;
  }

  public String getNamespacedAlias() {
    return namespacedAlias;
  }

  public String getTempAlias() {
    return tempAlias;
  }

  public boolean isId() {
    return isId;
  }

  // Classes

  // TODO: remove this class

  @Deprecated
  private static class Expression {

    private static final String AS = " as ";

    private String formula;
    private String originalName;
    private String newNamespacedAlias;

    public Expression(final String formula, final String originalName) {
      this.formula = formula;
      this.originalName = originalName;
    }

    public static List<Expression> parse(final String literal) throws ColumnAliasNotFoundException {
      List<Expression> exps = new ArrayList<Expression>();
      if (literal != null) {
        boolean inString = false;
        int start = 0;
        int i = start;
        while (i < literal.length()) {
          if (literal.charAt(i) == '\'') {
            inString = !inString;
          }
          if (!inString && literal.charAt(i) == ',') {
            String one = literal.substring(start, i);
            if (!SUtils.isEmpty(one)) {
              Expression e = parseOne(one);
              exps.add(e);
            }
            start = i + 1;
            i = start;
          } else {
            i++;
          }
        }
        String lastOne = literal.substring(start, i);
        if (!SUtils.isEmpty(lastOne)) {
          Expression e = parseOne(lastOne);
          exps.add(e);
        }
      }
      return exps;
    }

    private static Expression parseOne(final String one) throws ColumnAliasNotFoundException {
      int idx = one.toLowerCase().lastIndexOf(AS);
      if (idx == -1) {
        throw new ColumnAliasNotFoundException(one);
      } else {
        return new Expression(one.substring(0, idx).trim(), one.substring(idx + AS.length()).trim());
      }
    }

    public String getFormula() {
      return formula;
    }

    public String getOriginalName() {
      return originalName;
    }

    public String getNewNamespacedAlias() {
      return newNamespacedAlias;
    }

    public void setNewNamespacedAlias(String newNamespacedAlias) {
      this.newNamespacedAlias = newNamespacedAlias;
    }

  }

  private static class ColumnAliasNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    private String expression;

    public ColumnAliasNotFoundException(final String expression) {
      super();
      this.expression = expression;
    }

    public String getExpression() {
      return expression;
    }

  }

}
