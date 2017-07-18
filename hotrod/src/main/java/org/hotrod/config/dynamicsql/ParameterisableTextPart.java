package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.CollectionExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;

public class ParameterisableTextPart extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(ParameterisableTextPart.class);

  // Properties

  private String txt;

  protected List<SQLChunk> chunks = new ArrayList<SQLChunk>();

  // Constructor

  public ParameterisableTextPart(final String txt, final String tagIdentification,
      final ParameterDefinitions parameterDefinitions) throws InvalidConfigurationFileException {
    super("not-a-tag-but-sql-content");
    log.debug("init");
    this.txt = txt;
    this.validate(tagIdentification, parameterDefinitions);
  }

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No extra validation on the body
  }

  private void validate(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    int pos = 0;
    int prefix;
    int suffix;

    while (pos < this.txt.length() && (prefix = this.txt.indexOf(SQLParameter.PREFIX, pos)) != -1) {

      LiteralTextPart l = new LiteralTextPart(this.txt.substring(pos, prefix));
      this.chunks.add(l);

      suffix = this.txt.indexOf(SQLParameter.SUFFIX, prefix + SQLParameter.PREFIX.length());
      if (suffix == -1) {
        throw new InvalidConfigurationFileException(
            getErrorMessage(this.txt, tagIdentification, "Unmatched parameter delimiters; found an '"
                + SQLParameter.PREFIX + "' but not an '" + SQLParameter.SUFFIX + "'."));
      }

      SQLParameter p = new SQLParameter(this.txt.substring(prefix + SQLParameter.PREFIX.length(), suffix),
          tagIdentification, false);

      SQLParameter definition = parameterDefinitions.find(p);
      if (p.isDefinition()) {
        if (definition != null) {
          throw new InvalidConfigurationFileException("The body of the tag " + tagIdentification
              + " has multiple parameter definitions with the same name: " + p.getName() + ".\n"
              + "* If you want them to be different parameters, please choose a different names for each one;\n"
              + "* If you want to use the same parameter multiple times, "
              + "then the 'javaType' and/or 'jdbcType' can only be specified " + "on the first occurrence of it.");
        } else {
          parameterDefinitions.add(p);
        }
        this.chunks.add(p);
      } else {
        if (definition != null) {
          if (definition.isVariable()) {
            VariableOccurrence v = new VariableOccurrence(definition.getName());
            this.chunks.add(v);
          } else {
            p.setDefinition(definition);
            this.chunks.add(p);
          }
        } else {
          throw new InvalidConfigurationFileException(
              "The body of the tag " + tagIdentification + " includes a parameter reference '" + p.getName()
                  + "' but there's no parameter defined with that name yet.\n"
                  + "The first time a parameter is specified, " + "it must be fully qualified with the 'javaType' and "
                  + "'jdbcType' values (i.e. must be a parameter definition, rather than a parameter occurrence).");
        }
      }

      pos = suffix + SQLParameter.SUFFIX.length();
    }

    if (pos < this.txt.length()) {
      LiteralTextPart l = new LiteralTextPart(this.txt.substring(pos));
      this.chunks.add(l);
    }
  }

  public String getErrorMessage(final String txt, final String tagIdentification, final String extraMessage) {
    return "Invalid SQL query as the body of the tag " + tagIdentification + ": invalid parameter section '" + txt
        + "'. Can include one or more parameters with the form: " + SQLParameter.PREFIX
        + "name,javaType=<JAVA-CLASS>,jdbcType=<JDBC-TYPE>" + SQLParameter.SUFFIX
        + (extraMessage == null ? "" : ":\n" + extraMessage);
  }

  public boolean isEmpty() {
    for (SQLChunk ch : this.chunks) {
      if (!ch.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return false;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    ParameterRenderer conditionRenderer = new ParameterRenderer() {
      @Override
      public String render(SQLParameter parameter) {
        return parameter.getName();
      }
    };
    StringBuilder sb = new StringBuilder();
    for (SQLChunk ch : this.chunks) {
      sb.append(ch.renderStatic(conditionRenderer));
    }
    String text = sb.toString();
    return text;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    ParameterRenderer conditionRenderer = new ParameterRenderer() {
      @Override
      public String render(SQLParameter parameter) {
        return parameter.getName();
      }
    };
    StringBuilder sb = new StringBuilder();
    for (SQLChunk ch : this.chunks) {
      sb.append(ch.renderXML(conditionRenderer));
    }
    String text = sb.toString();
    return text;
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {

    List<DynamicExpression> exprs = new ArrayList<DynamicExpression>();
    LiteralExpression last = null;
    for (SQLChunk p : this.chunks) {
      DynamicExpression expr = p.getJavaExpression(parameterRenderer);
      try {
        LiteralExpression v = (LiteralExpression) expr;
        if (last == null) {
          last = v;
        } else {
          last = last.concat(v);
        }
      } catch (ClassCastException e) {
        if (last != null) {
          exprs.add(last);
          last = null;
        }
        exprs.add(expr);
      }
    }
    if (last != null) {
      exprs.add(last);
    }

    return new CollectionExpression(exprs.toArray(new DynamicExpression[0]));

  }

}
