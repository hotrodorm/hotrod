package xsdtests.case04;

import java.io.File;
import java.net.MalformedURLException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import xsdtests.StrictValidationEventHandler;

public class XMLLoader4 {

  private static final File dir = new File("testdata/xsd-parser/case004");

  public void parse() throws JAXBException, MalformedURLException, SAXException {
    System.out.println("[--- Will parse now ---]\n");

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(new StreamSource(new File(dir, "grammar.xsd")));

    JAXBContext context = JAXBContext.newInstance(Employee.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();

    unmarshaller.setEventHandler(new StrictValidationEventHandler());
    unmarshaller.setSchema(schema);

    File f = new File(dir, "file.xml");
    Employee bean = (Employee) unmarshaller.unmarshal(f);

    System.out.println("Bean: " + bean);
    for (Alias a : bean.getAliases()) {
      System.out.println("alias: " + a);
    }

    System.out.println("\n[--- Parsed ---]");
  }

}
