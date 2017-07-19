package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.ParameterisableTextPart;
import org.hotrod.config.dynamicsql.TagAttribute;
import org.hotrod.exceptions.InvalidConfigurationFileException;

import xsdtests.case00.Myelement;
import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

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
  public void render(final StringBuilder sb) throws InvalidConfigurationFileException {
    ParameterDefinitions defs = new ParameterDefinitions();
    RendererHelper.renderHeader("yayent", sb,
        new TagAttribute("myattribute", new ParameterisableTextPart(this.myattribute, "yayent", defs)));
    for (Myelement e : this.myelements) {
      e.render(sb);
    }
    RendererHelper.renderFooter("yayent", sb);
  }

}
