package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.TagAttribute;
import org.hotrod.exceptions.InvalidConfigurationFileException;

import xsdtests.case00.Myelement;
import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

@XmlRootElement
public class Yayeyt implements RendereableTag {

  private String myattribute;

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = Myelement.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public String getMyattribute() {
    return myattribute;
  }

  @XmlAttribute
  public void setMyattribute(String myattribute) {
    this.myattribute = myattribute;
  }

  public List<Object> getContent() {
    return this.content;
  }

  // Rendering

  @Override
  public void render(final StringBuilder sb) throws InvalidConfigurationFileException {
    ParameterDefinitions defs = new ParameterDefinitions();
    RendererHelper.render("yayeyt", sb, this.content, new TagAttribute("myattribute", this.myattribute));
  }

}
