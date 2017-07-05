package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.Myelement;

@XmlRootElement
public class Nayent {

  private List<Myelement> myelements = new ArrayList<Myelement>();

  // Getters & Setters

  public List<Myelement> getMyelements() {
    return myelements;
  }

  @XmlElement
  public void setMyelement(Myelement myelement) {
    this.myelements.add(myelement);
  }

}
