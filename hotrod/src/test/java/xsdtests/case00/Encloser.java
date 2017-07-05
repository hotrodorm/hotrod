package xsdtests.case00;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import xsdtests.case00.combinations.Nanent;
import xsdtests.case00.combinations.Naneyt;
import xsdtests.case00.combinations.Nayent;
import xsdtests.case00.combinations.Nayeyt;
import xsdtests.case00.combinations.Yanent;
import xsdtests.case00.combinations.Yaneyt;
import xsdtests.case00.combinations.Yayent;
import xsdtests.case00.combinations.Yayeyt;

@XmlRootElement
public class Encloser {

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = Nanent.class), //
      @XmlElementRef(type = Naneyt.class), //
      @XmlElementRef(type = Nayent.class), //
      @XmlElementRef(type = Nayeyt.class), //
      @XmlElementRef(type = Yanent.class), //
      @XmlElementRef(type = Yaneyt.class), //
      @XmlElementRef(type = Yayent.class), //
      @XmlElementRef(type = Yayeyt.class), //
  })
  private List<Object> content = new ArrayList<Object>();

}
