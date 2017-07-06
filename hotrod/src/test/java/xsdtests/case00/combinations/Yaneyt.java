package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;
import xsdtests.case10.configuration.TagAttribute;

@XmlRootElement
public class Yaneyt implements RendereableTag {

  private String myattribute = null;
  private String body = null;

  // Getters & Setters

  public String getMyAttribute() {
    return this.myattribute;
  }

  @XmlAttribute
  public void setMyattribute(String myattribute) {
    this.myattribute = myattribute;
  }

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    this.body = body;
  }

  // Rendering

  @Override
  public void render(final StringBuilder sb) {
    RendererHelper.render("yaneyt", sb, this.body, new TagAttribute("myattribute", this.myattribute));
  }

}
