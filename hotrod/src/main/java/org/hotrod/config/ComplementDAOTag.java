package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.OtherwiseTag;
import org.hotrod.config.dynamicsql.SetTag;
import org.hotrod.config.dynamicsql.TagAttribute;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.WhenTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

@XmlRootElement(name = "complement")
public class ComplementDAOTag extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

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
  protected transient List<Object> content = new ArrayList<Object>();

  private List<DynamicSQLPart> parts;

  // Constructor

  public ComplementDAOTag() {
    super("complement");
  }

  // @Override
  // public void validate(final DaosTag daosTag, final HotRodConfigTag config,
  // final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions
  // parameters)
  // throws InvalidConfigurationFileException {
  //
  // this.parts = new ArrayList<DynamicSQLPart>();
  // for (Object obj : this.content) {
  // DynamicSQLPart p = null;
  // try {
  // String s = (String) obj;
  // p = new ParameterisableTextPart(s, this.getSourceLocation(), parameters);
  // } catch (ClassCastException e1) {
  // try {
  // p = (DynamicSQLPart) obj;
  // p.retrievePartsAndValidate(parameters);
  // } catch (ClassCastException e2) {
  // throw new InvalidConfigurationFileException(super.getSourceLocation(),
  // "Malformed content of the <"
  // + this.getTagName() + "> tag. Invalid inner tag of class " +
  // obj.getClass().getName());
  // }
  // }
  // this.parts.add(p);
  // }
  //
  // }

  @Override
  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return "";
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderXML(parameterRenderer));
    }
    return sb.toString();
  }

  @Override
  public DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    // XXX: Pending. Develop only when/if it's needed.
    return null;
  }

  @Override
  protected void validateAttributes(ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // XXX: Pending. Develop only when/if it's needed.
  }

  @Override
  protected void specificBodyValidation(ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // XXX: Pending. Develop only when/if it's needed.
  }

  @Override
  protected boolean shouldRenderTag() {
    // XXX: Pending. Develop only when/if it's needed.
    return false;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    // XXX: Pending. Develop only when/if it's needed.
    return null;
  }

  // Merging logic

  @Override
  protected boolean sameProperties(final DynamicSQLPart fresh) {
    return true;
  }

}