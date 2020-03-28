package xsdtests.case00.combinations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;

import xsdtests.case00.Myelement;
import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

@XmlRootElement
public class Nayeyt implements RendereableTag {

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = Myelement.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public List<Object> getContent() {
    return this.content;
  }

  // Rendering

  @Override
  public void render(final StringBuilder sb) throws InvalidConfigurationFileException {
    RendererHelper.render("nayeyt", sb, this.content);
  }

}
