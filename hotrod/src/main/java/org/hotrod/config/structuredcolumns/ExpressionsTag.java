package org.hotrod.config.structuredcolumns;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.ExpressionsMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;

@XmlRootElement(name = "expressions")
public class ExpressionsTag extends AbstractConfigurationTag implements ColumnsProvider {

  // Constants

  private static final Logger log = Logger.getLogger(ExpressionsTag.class);

  // Properties

  private List<Expression> expressions;

  protected ColumnsMetadataRetriever columnsRetriever;
  private List<StructuredColumnMetadata> columnsMetadata;

  private HotRodGenerator generator;

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = ColumnTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  private List<ColumnTag> columns = null;

  private String literalContent;

  // Constructor

  public ExpressionsTag() {
    super("expressions");
  }

  // JAXB Setters

  // Behavior

  // final DatabaseAdapter adapter
  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult)
      throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: expressions

    this.expressions = new ArrayList<Expression>();
    this.literalContent = "";
    this.columns = new ArrayList<ColumnTag>();
    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        log.debug("*** s=" + s);
        if (!SUtils.isEmpty(s)) {
          if (!this.literalContent.isEmpty()) {
            this.literalContent = this.literalContent + " ";
          }
          this.literalContent = this.literalContent + s;
          try {
            List<Expression> es = Expression.parse(s);
            this.expressions.addAll(es);
          } catch (WildCardNotSupportedException e) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(),
                "The wildcard symbol '*' is not supported in the body of an <" + super.getTagName() + "> tag. .");
          } catch (ColumnAliasNotFoundException e) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(),
                "Expression without an alias. The expression '" + e.getExpression() + "' must declare a name alias.");
          }
        }
      } catch (ClassCastException e1) {
        try {
          ColumnTag col = (ColumnTag) obj; // column
          this.columns.add(col);
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
              + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
        }
      }
    }

    // expressions in body

    if (SUtils.isEmpty(this.literalContent)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid empty <" + super.getTagName() + "> tag. " + "When specified this tag must not be empty.");
    }

    // column

    for (ColumnTag t : this.columns) {
      t.validate(config);
    }

  }

  @Override
  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    this.generator = generator;
  }

  // Meta data gathering

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {
    this.columnsRetriever = new ColumnsMetadataRetriever(selectTag, this.generator.getAdapter(),
        this.generator.getJdbcDatabase(), this.generator.getLoc(), selectGenerationTag, this, null,
        columnsPrefixGenerator);
    this.columnsRetriever.prepareRetrieval(conn1);
  }

  @Override
  public void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException, ControlledException {
    List<StructuredColumnMetadata> exps = this.columnsRetriever.retrieve(conn2);

    log.debug("expressions=" + exps.size());

    // Check column tags actually have related expressions

    for (ColumnTag t : this.columns) {
      StructuredColumnMetadata exp = findExpression(t.getName(), exps);
      if (exp == null) {
        throw new ControlledException("Invalid <" + new ColumnTag().getTagName() + "> tag with name '" + t.getName()
            + "' at " + t.getSourceLocation().render() + ".\n" + "There's no expression with the name '" + t.getName()
            + "'.");
      }
    }

    // Apply column tags to meta data

    this.columnsMetadata = new ArrayList<StructuredColumnMetadata>();
    for (StructuredColumnMetadata exp : exps) {
      ColumnTag t = findColumnTag(exp.getColumnName());

      if (t == null) {
        log.debug("[exp] " + exp.getColumnName() + " t=null");
      } else {
        log.debug("[exp] " + exp.getColumnName() + " t=" + t.getName() + " (" + t.getJavaType() + ")");
      }

      if (t == null) {
        this.columnsMetadata.add(exp);
      } else {
        StructuredColumnMetadata scm = StructuredColumnMetadata.applyColumnTag(exp, t);
        this.columnsMetadata.add(scm);
      }
    }

    // apply new namespaced alias to the expressions

    for (Expression e : this.expressions) {
      StructuredColumnMetadata m = findColumn(e.getOriginalName());
      if (m == null) {
        throw new ControlledException(
            "Invalid <" + super.getTagName() + "> tag. Could not find column '" + e.getOriginalName() + "'.");
      }
      e.setNewNamespacedAlias(m.getColumnAlias());
      m.setFormula(e.getFormula());
    }

  }

  private StructuredColumnMetadata findColumn(final String originalName) {
    for (StructuredColumnMetadata m : this.columnsMetadata) {
      if (this.generator.getAdapter().isColumnIdentifier(m.getColumnName(), originalName)) {
        return m;
      }
    }
    return null;
  }

  private StructuredColumnMetadata findExpression(final String configName, final List<StructuredColumnMetadata> exps) {
    for (StructuredColumnMetadata m : exps) {
      if (this.generator.getAdapter().isColumnIdentifier(m.getColumnName(), configName)) {
        return m;
      }
    }
    return null;
  }

  private ColumnTag findColumnTag(final String expressionName) {
    for (ColumnTag t : this.columns) {
      if (this.generator.getAdapter().isColumnIdentifier(expressionName, t.getName())) {
        return t;
      }
    }
    return null;
  }

  // Rendering

  // TODO

  public List<String> gelAliasedSQLColumns() {
    List<String> columns = new ArrayList<String>();
    for (Expression e : this.expressions) {
      log.info("=================== e.getFormula()=" + e.getFormula());
      columns.add(e.getFormula() + " as " + e.getNewNamespacedAlias());
    }
    return columns;
  }

  // Getters

  public ExpressionsMetadata getExpressionsMetadata() {
    return new ExpressionsMetadata(this.columnsMetadata);
  }

  @Override
  public String renderColumns() {
    return this.literalContent;
  }

  public List<StructuredColumnMetadata> getColumnsMetadata() {
    return columnsMetadata;
  }

  // Classes

  private static class Expression {

    private static final String AS = " as ";

    private String formula;
    private String originalName;
    private String newNamespacedAlias;

    public Expression(final String formula, final String originalName) {
      this.formula = formula;
      this.originalName = originalName;
    }

    public static List<Expression> parse(final String literal)
        throws WildCardNotSupportedException, ColumnAliasNotFoundException {
      List<Expression> exps = new ArrayList<Expression>();
      if (literal != null) {
        boolean inString = false;
        int start = 0;
        int i = start;
        while (i < literal.length()) {
          if (!inString && literal.charAt(i) == '*') {
            throw new WildCardNotSupportedException();
          }
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

  private static class WildCardNotSupportedException extends Exception {
    private static final long serialVersionUID = 1L;
  }

}
