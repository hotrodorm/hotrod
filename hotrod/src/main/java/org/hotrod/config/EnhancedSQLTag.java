package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.OtherwiseTag;
import org.hotrod.config.dynamicsql.SetTag;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.WhenTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.config.sqlcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

public abstract class EnhancedSQLTag extends AbstractConfigurationTag {

  // Properties

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = IfTag.class), //
      @XmlElementRef(type = ChooseTag.class), //
      @XmlElementRef(type = WhereTag.class), //
      @XmlElementRef(type = WhenTag.class), //
      @XmlElementRef(type = OtherwiseTag.class), //
      @XmlElementRef(type = ForEachTag.class), //
      @XmlElementRef(type = BindTag.class), //
      @XmlElementRef(type = SetTag.class), //
      @XmlElementRef(type = TrimTag.class) //
  })
  protected List<Object> content = new ArrayList<Object>();

  protected List<EnhancedSQLTag> eparts = null;

  // Constructor

  protected EnhancedSQLTag(final String tagName) {
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

  public abstract String renderXML(final ParameterRenderer parameterRenderer);

  // Java Expression Rendering

  public String renderJavaExpression(final int margin, final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    DynamicExpression expr = this.getJavaExpression(parameterRenderer);
    return expr.renderConstructor(margin);
  }

  public abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException;

}
