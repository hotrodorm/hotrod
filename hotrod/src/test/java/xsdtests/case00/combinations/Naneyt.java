package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Naneyt {

  private String body = null;

  // Getters & Setters

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(final String body) {
    this.body = body;
  }

}
