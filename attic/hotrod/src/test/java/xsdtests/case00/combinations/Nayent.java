package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.Myelement;
import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

@XmlRootElement
public class Nayent implements RendereableTag {

  private List<Myelement> myelements = new ArrayList<Myelement>();

  // Getters & Setters

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
    RendererHelper.renderHeader("nayent", sb);
    for (Myelement e : this.myelements) {
      e.render(sb);
    }
    RendererHelper.renderFooter("nayent", sb);
  }

}
