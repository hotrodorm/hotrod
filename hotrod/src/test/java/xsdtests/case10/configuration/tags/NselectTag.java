package xsdtests.case10.configuration.tags;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "select")
public class NselectTag extends AbstractConfigurationTag {

  protected NselectTag() {
    super("select");
  }

  private String name = null;
  private String vo = null;

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = IfTag.class), //
      @XmlElementRef(type = ChooseTag.class), //
      @XmlElementRef(type = WhereTag.class), //
      @XmlElementRef(type = ForEachTag.class), //
      @XmlElementRef(type = BindTag.class), //
      @XmlElementRef(type = TrimTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public String getName() {
    return name;
  }

  @XmlAttribute
  public void setName(String name) {
    this.name = name;
  }

  public String getVo() {
    return vo;
  }

  @XmlAttribute
  public void setVo(String vo) {
    this.vo = vo;
  }

  public List<Object> getContent() {
    return this.content;
  }

  // render

  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("<select name=\"" + this.name + "\" vo=\"" + this.vo + "\">");

    for (Object obj : this.content) {

      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLPart t = (DynamicSQLPart) obj;
          sb.append(t.renderSQLSentence(new ParameterRenderer() {

            @Override
            public String render(SQLParameter parameter) {
              return SQLParameter.PREFIX + parameter.getName() + SQLParameter.SUFFIX;
            }

          }));
        } catch (ClassCastException e2) {
          sb.append("[could not render tag of class: " + obj.getClass().getName() + " ]");
        }
      }

    }

    sb.append("</select>");

    return sb.toString();
  }

  // toString

}
