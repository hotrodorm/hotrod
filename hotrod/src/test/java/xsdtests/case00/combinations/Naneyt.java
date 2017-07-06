package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

@XmlRootElement
public class Naneyt implements RendereableTag {

  private String body = null;

  // Getters & Setters

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(final String body) {
    this.body = body;
  }

  // Rendering

  @Override
  public void render(final StringBuilder sb) {
    RendererHelper.render("naneyt", sb, this.body);
  }

}
