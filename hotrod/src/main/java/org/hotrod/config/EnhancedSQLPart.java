package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;

public abstract class EnhancedSQLPart extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(EnhancedSQLPart.class);

  // Properties

  protected transient List<Object> content = new ArrayList<Object>();

  protected List<EnhancedSQLPart> eparts = null;

  // Constructor

  protected EnhancedSQLPart(final String tagName) {
    super(tagName);
  }

  // Validation

  public abstract void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters)
      throws InvalidConfigurationFileException;

  public abstract void validateAgainstDatabase(final HotRodGenerator generator)
      throws InvalidConfigurationFileException;

  // Render SQL Angle

  public abstract String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp);

  // Rendering

  public abstract String renderStatic(final ParameterRenderer parameterRenderer);

  public abstract void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer);

  // Java Expression Rendering

  public String renderJavaExpression(final int margin, final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    DynamicExpression expr = this.getJavaExpression(parameterRenderer);
    return expr.renderConstructor(margin);
  }

  public abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException;

  // Rendering

  public static class SQLFormatter {

    private List<String> lines = new ArrayList<String>();

    public void add(final String txt) {
      if (txt == null) {
        return;
      }
      if (txt.contains("\n")) {
        for (String l : txt.split("\n")) {
          this.lines.add(l);
        }
      } else {
        this.lines.add(txt);
      }
    }

    public int getCurrentIndent() {
      return this.lines.isEmpty() ? 0 : this.lines.get(this.lines.size() - 1).length();
    }

    public String toString() {
      ListWriter w = new ListWriter("\n");
      for (String line : this.lines) {
        if (!SUtils.isEmpty(line)) {
          w.add(line);
        }
      }
      return w.toString();
    }

  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      EnhancedSQLPart f = (EnhancedSQLPart) fresh;
      boolean different = !same(fresh);

      this.eparts = f.eparts;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      EnhancedSQLPart f = (EnhancedSQLPart) fresh;
      boolean same = Compare.same(this.eparts, f.eparts);
      log.debug("[" + this.getTagName() + "[" + (this.eparts == null ? "null" : "" + this.eparts.size()) + "] ] same="
          + same);
      return same;
    } catch (ClassCastException e) {
      return false;
    }
  }

}
