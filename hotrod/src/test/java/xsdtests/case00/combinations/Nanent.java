package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.RendereableTag;
import xsdtests.case00.RendererHelper;

@XmlRootElement
public class Nanent implements RendereableTag {

  // Rendering

  @Override
  public void render(final StringBuilder sb) {
    RendererHelper.render("nanent", sb);
  }

}
