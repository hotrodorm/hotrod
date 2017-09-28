package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "columns")
public class ColumnsTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsTag.class);

  // Properties

  private String voClass = null;

  private List<VOTag> vos = new ArrayList<VOTag>();
  private List<ExpressionsTag> expressions = new ArrayList<ExpressionsTag>();

  // Constructor

  public ColumnsTag() {
    super("columns");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "vo-class")
  public void setVoClass(final String voClass) {
    this.voClass = voClass;
  }

  @XmlElement(name = "vo")
  public void setVO(final VOTag vo) {
    this.vos.add(vo);
  }

  @XmlElement(name = "expressions")
  public void setExpressionsTag(final ExpressionsTag exps) {
    this.expressions.add(exps);
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // vo-class

    if (this.voClass != null) {
      if (!this.voClass.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid Java class '" + this.voClass
                + "' specified in the vo-class attribute. When specified, the vo-class must start with an upper case letter, "
                + "and continue with any combination of letters, digits, or underscores.");
      }
    }

    // vos

    for (VOTag vo : this.vos) {
       vo.validate(daosTag, config, fragmentConfig);
    }

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validate(config);
    }

  }

  // Getters

  public String getVoClass() {
    return voClass;
  }

  public List<VOTag> getVos() {
    return vos;
  }

  public List<ExpressionsTag> getExpressions() {
    return expressions;
  }

}
