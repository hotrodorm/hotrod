package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.dynamicsql.TagAttribute;

import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

@XmlRootElement
public class Yanent implements RendereableTag {

  private String myattribute = null;

  // Getters & Setters

  public String getMyattribute() {
    return myattribute;
  }

  @XmlAttribute
  public void setMyattribute(String myattribute) {
    this.myattribute = myattribute;
  }

  // Rendering

  @Override
  public void render(final StringBuilder sb) {
    RendererHelper.render("yanent", sb, new TagAttribute("myattribute", this.myattribute));
  }

}
