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
import org.hotrod.utils.Compare;

@XmlRootElement(name = "complement")
public class ComplementTag extends EnhancedSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(ComplementTag.class);

  // Properties - Primitive content parsing by JAXB

  // TODO: make this property transient. JAXB fails when doing so with the
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
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters)
      throws InvalidConfigurationFileException {

    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
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
    }

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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      ComplementTag f = (ComplementTag) fresh;
      boolean different = !same(fresh);
      this.content = f.content;
      this.parts = f.parts;
      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      ComplementTag f = (ComplementTag) fresh;
      return Compare.same(this.parts, f.parts);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}