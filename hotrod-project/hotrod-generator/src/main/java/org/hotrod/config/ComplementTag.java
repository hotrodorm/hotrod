package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.Metadata;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "complement")
public class ComplementTag extends EnhancedSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ComplementTag.class);

  // Properties - Primitive content parsing by JAXB

  // This property cannot be transient. JAXB fails when doing so with the
  // message: Transient field "content" cannot have any JAXB annotations.

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
    log.debug("init");
  }

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {

    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
      DynamicSQLPart p = null;
      try {
        String s = (String) obj; // literal content part
        p = new ParameterisableTextPart(s, this, parameters);
      } catch (ClassCastException e1) {
        try {
          p = (DynamicSQLPart) obj; // dynamic sql part
          p.retrievePartsAndValidate(parameters);
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException(this, "Malformed content of the <" + this.getTagName()
              + "> tag. Invalid inner tag of class " + obj.getClass().getName());
        }
      }
      this.parts.add(p);
      super.addChild(p);
    }

  }

  @Override
  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {
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
  public void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderXML(parameterRenderer));
    }
    formatter.add(sb.toString());
  }

  @Override
  public DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    // XXX: Pending. Develop only when/if it's needed.
    return null;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}