package xsdtests.case07;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Foo {

  public Foo() {
    System.out.println("*** [Constructor] " + Foo.class.getSimpleName());
  }

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = Bar.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public List<Object> getContent() {
    return this.content;
  }

}