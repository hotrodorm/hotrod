package xsdtests.case07;

import java.io.File;
import java.net.MalformedURLException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.hotrod.config.StrictValidationEventHandler;
import org.xml.sax.SAXException;

public class XMLLoader7 {

  private static final File dir = new File("testdata/xsd-parser/case007");

  public void parse() throws JAXBException, MalformedURLException, SAXException {
    System.out.println("[--- Will parse now ---]\n");

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(new StreamSource(new File(dir, "grammar.xsd")));

    JAXBContext context = JAXBContext.newInstance(Foo.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();

    unmarshaller.setEventHandler(new StrictValidationEventHandler());
    unmarshaller.setSchema(schema);

    File f = new File(dir, "file.xml");
    Foo bean = (Foo) unmarshaller.unmarshal(f);

    System.out.println("Bean: " + bean);
    for (Object obj : bean.getContent()) {
      System.out.println("obj=" + obj);
    }

    System.out.println("\n[--- Parsed ---]");
  }

}
