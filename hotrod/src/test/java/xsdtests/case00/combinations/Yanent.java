package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Yanent {

  private String myattribute = null;

  // Getters & Setters

  public String getMyattribute() {
    return myattribute;
  }

  @XmlAttribute
  public void setMyattribute(String myattribute) {
    this.myattribute = myattribute;
  }

}
