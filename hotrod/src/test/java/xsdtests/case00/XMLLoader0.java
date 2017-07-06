package xsdtests.case00;

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

public class XMLLoader0 {

  private static final File dir = new File("testdata/xsd-parser/case000");

  public void parse() throws JAXBException, MalformedURLException, SAXException {
    System.out.println("[--- Will parse now ---]\n");

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(new StreamSource(new File(dir, "grammar.xsd")));

    JAXBContext context = JAXBContext.newInstance(Encloser.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();

    unmarshaller.setEventHandler(new StrictValidationEventHandler());
    unmarshaller.setSchema(schema);

    File f = new File(dir, "file.xml");
    Encloser bean = (Encloser) unmarshaller.unmarshal(f);

    System.out.println("Bean: " + bean);

    StringBuilder sb = new StringBuilder();
    bean.render(sb);
    System.out.println("Bean: " + sb.toString());

    System.out.println("\n[--- Parsed ---]");
  }

}
