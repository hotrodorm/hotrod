package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.sqlcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.CollectionExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "not-a-tag")
public class SequenceOfParts extends EnhancedSQLTag {

  // Constructor

  public SequenceOfParts(final List<EnhancedSQLTag> parts) {
    super("not-a-tag");
    super.eparts = parts;
  }

  // Behavior

  // Rendering

  // Java Expression

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new CollectionExpression(toArray(this.eparts, parameterRenderer));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for XML tag on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  private DynamicExpression[] toArray(final List<EnhancedSQLTag> parts, final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    List<DynamicExpression> exps = new ArrayList<DynamicExpression>();
    LiteralExpression last = null;
    for (EnhancedSQLTag p : parts) {
      DynamicExpression expr = p.getJavaExpression(parameterRenderer);
      try {
        LiteralExpression le = (LiteralExpression) expr;
        if (last == null) {
          last = le;
        } else {
          last.concat(le);
        }
      } catch (ClassCastException e) {
        if (last != null) {
          exps.add(last);
          last = null;
        }
        exps.add(expr);
      }
    }
    if (last != null) {
      exps.add(last);
    }
    return exps.toArray(new DynamicExpression[0]);
  }

  @Override
  public void validate(DaosTag daosTag, HotRodConfigTag config, HotRodFragmentConfigTag fragmentConfig,
      ParameterDefinitions parameters) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public void validateAgainstDatabase(HotRodGenerator generator) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public String renderSQLAngle(DatabaseAdapter adapter, ColumnsProvider cp) {
    // Nothing to do
    return null;
  }

  @Override
  public String renderStatic(ParameterRenderer parameterRenderer) {
    // Nothing to do
    return null;
  }

  @Override
  public String renderXML(ParameterRenderer parameterRenderer) {
    // TODO Auto-generated method stub
    return null;
  }

}