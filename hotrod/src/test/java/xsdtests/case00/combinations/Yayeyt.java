package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.Myelement;

@XmlRootElement
public class Yayeyt {

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

}
