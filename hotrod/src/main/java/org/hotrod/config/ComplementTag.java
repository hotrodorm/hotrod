package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.OtherwiseTag;
import org.hotrod.config.dynamicsql.ParameterisableTextPart;
import org.hotrod.config.dynamicsql.SetTag;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.WhenTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "complement")
public class ComplementTag extends EnhancedSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(ComplementTag.class);

  // Properties - Primitive content parsing by JAXB

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

  // Properties - Parsed

  private List<DynamicSQLPart> parts;

  // Constructor

  public ComplementTag() {
    super("complement");
  }

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters)
      throws InvalidConfigurationFileException {

    log.info("VALIDATE: start - this.content.size()=" + this.content.size());

    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
      log.info("obj: " + obj.getClass().getName());
      DynamicSQLPart p = null;
      try {
        String s = (String) obj;
        p = new ParameterisableTextPart(s, this.getSourceLocation(), parameters);
      } catch (ClassCastException e1) {
        try {
          p = (DynamicSQLPart) obj;
          p.retrievePartsAndValidate(parameters);
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(), "Malformed content of the <"
              + this.getTagName() + "> tag. Invalid inner tag of class " + obj.getClass().getName());
        }
      }
      this.parts.add(p);
      log.info("VALIDATE: add - " + p.getClass().getName());
    }
    log.info("VALIDATE: total=" + this.parts.size());

  }

  @Override
  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp) {
    return "";
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return "";
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    log.info("[complement]");
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      log.info("DynamicSQLPart: " + p.getClass().getName());
      sb.append(p.renderXML(parameterRenderer));
    }
    return sb.toString();
  }

  @Override
  public DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    // TODO Auto-generated method stub
    return null;
  }

}