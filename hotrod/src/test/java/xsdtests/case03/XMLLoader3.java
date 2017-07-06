package xsdtests.case03;

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

public class XMLLoader3 {

  private static final File dir = new File("testdata/xsd-parser/case003");

  public void parse() throws JAXBException, MalformedURLException, SAXException {
    System.out.println("[--- Will parse now ---]\n");

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(new StreamSource(new File(dir, "grammar.xsd")));

    JAXBContext context = JAXBContext.newInstance(Product.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();

    unmarshaller.setEventHandler(new StrictValidationEventHandler());
    unmarshaller.setSchema(schema);

    File f = new File(dir, "file.xml");
    Product bean = (Product) unmarshaller.unmarshal(f);

    System.out.println("Bean: " + bean);

    System.out.println("\n[--- Parsed ---]");
  }

}
