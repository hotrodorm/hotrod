package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.Myelement;

@XmlRootElement
public class Nayeyt {

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = Myelement.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public List<Object> getContent() {
    return this.content;
  }

}
