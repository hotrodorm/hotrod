package xsdtests.case00;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Myelement implements RendereableTag {

  // Rendering

  @Override
  public void render(final StringBuilder sb) {
    sb.append("<myelement/>");
  }

}
