package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.Myelement;
import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;
import xsdtests.case10.configuration.TagAttribute;

@XmlRootElement
public class Yayent implements RendereableTag {

  private String myattribute = null;
  private List<Myelement> myelements = new ArrayList<Myelement>();

  // Getters & Setters

  public String getMyattribute() {
    return myattribute;
  }

  @XmlAttribute
  public void setMyattribute(String myattribute) {
    this.myattribute = myattribute;
  }

  public List<Myelement> getMyelements() {
    return myelements;
  }

  @XmlElement
  public void setMyelement(Myelement myelement) {
    this.myelements.add(myelement);
  }

  // Rendering

  @Override
  public void render(final StringBuilder sb) {
    RendererHelper.renderHeader("yayent", sb, new TagAttribute("myattribute", this.myattribute));
    for (Myelement e : this.myelements) {
      e.render(sb);
    }
    RendererHelper.renderFooter("yayent", sb);
  }

}
