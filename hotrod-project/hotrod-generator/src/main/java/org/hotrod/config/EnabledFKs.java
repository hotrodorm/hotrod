package org.hotrod.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractHotRodConfigTag.LocationListener;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class EnabledFKs {

  private static final String FKS_FILE_NAME = "enabled-fks.xml";
  private static final String FKS_XSD_SCHEMA = "foreign-keys.xsd";

  private static final String DEBUG_PATH = "/";
  private static final String PLUGIN_PATH = "/xml/";

  private static final Logger log = LogManager.getLogger(EnabledFKs.class);

  private ForeignKeysTag fks;

  private EnabledFKs(final ForeignKeysTag fks) {
    this.fks = fks;
  }

  public static EnabledFKs loadIfPresent(final File baseDir) throws UncontrolledException, ControlledException {

    // Check if the file is present

    File f = new File(baseDir, FKS_FILE_NAME);
    if (!f.exists()) {
      return null;
    }

    // Prepare the parser

    Unmarshaller unmarshaller = null;
    XMLStreamReader xsr = null;
    StrictValidationEventHandler validationHandler = new StrictValidationEventHandler();

    // Load the schema

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    LSResourceResolver xsdResolver = new XSDResolver();
    factory.setResourceResolver(xsdResolver);
    InputStream is = null;
    try {
      is = ConfigurationLoader.class.getResourceAsStream(FKS_XSD_SCHEMA);
      Schema schema = factory.newSchema(new StreamSource(is));

      //

      JAXBContext context = JAXBContext.newInstance(ForeignKeysTag.class);

      unmarshaller = context.createUnmarshaller();
      unmarshaller.setEventHandler(validationHandler);
      unmarshaller.setSchema(schema);

      XMLInputFactory xif = XMLInputFactory.newFactory();
      FileInputStream xml = new FileInputStream(f);
      xsr = xif.createXMLStreamReader(xml);
      LocationListener locationListener = new LocationListener(f, xsr);
      unmarshaller.setListener(locationListener);

    } catch (SAXException e) {
      throw new UncontrolledException("Could not load enabled-foreign-keys file [internal XML parser error]", e);
    } catch (JAXBException e) {
      throw new UncontrolledException("Could not load enabled-foreign-keys file [internal XML parser error]", e);
    } catch (XMLStreamException e) {
      String message = (e.getLocation() == null ? ""
          : "[line " + e.getLocation().getLineNumber() + ", col " + e.getLocation().getColumnNumber() + "] ")
          + e.getMessage();
      throw new ControlledException(
          Constants.TOOL_NAME + " configuration file '" + f.getPath() + "' is not well-formed: " + message);
    } catch (FileNotFoundException e) {
      throw new UncontrolledException("Could not load enabled-foreign-keys file; file '" + f + "' not found", e);
    }

    // Parse the configuration file

    try {

      log.debug("[ Will parse ]");
      ForeignKeysTag fks = (ForeignKeysTag) unmarshaller.unmarshal(xsr);
      log.debug("[ Parsed ]");

      // Validation (specific)

      log.debug("Will validate semantics.");
      fks.validate();
      log.debug("Semantics validation #1 successful.");

      // Complete

      log.debug("File loaded.");

      return new EnabledFKs(fks);

    } catch (JAXBException e) {
      throw assembleControlledException(f, validationHandler, e);

    } catch (InvalidConfigurationFileException e) {
      SourceLocation loc = e.getTag().getSourceLocation();
      log.debug("loc=" + loc);
      if (loc == null) {
        throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());
      } else {
        throw new ControlledException(loc, e.getMessage());
      }

    }

  }

  private static ControlledException assembleControlledException(final File f,
      final StrictValidationEventHandler validationHandler, final JAXBException e) {
    ValidationEventLocator locator = validationHandler.getLocator();

    SourceLocation location = null;

    if (locator == null) {
      try {
        UnmarshalException ue = (UnmarshalException) e;
        if (ue.getCause() != null) {
          XMLStreamException xe = (XMLStreamException) ue.getCause();
          Location lxe = xe.getLocation();
          location = new SourceLocation(f, lxe.getLineNumber(), lxe.getColumnNumber(), lxe.getCharacterOffset());
          return new ControlledException(location, xe.getMessage());
        }
      } catch (ClassCastException e2) {
        // Ignore
      }
    } else {
      location = new SourceLocation(f, locator.getLineNumber(), locator.getColumnNumber(), locator.getOffset());
    }

    if (e.getMessage() != null) {
      return new ControlledException(location, e.getMessage());
    } else if (e.getCause() != null) {
      try {
        SAXParseException pe = (SAXParseException) e.getCause();
        return new ControlledException(location, pe.getMessage());
      } catch (ClassCastException e2) {
        return new ControlledException(location, e.getCause().getMessage());
      }
    } else {
      return new ControlledException(location, "Invalid configuration file.");
    }
  }

  // Getters

  public ForeignKeysTag getFKs() {
    return fks;
  }

  // Inner classes

  private static class XSDResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(final String type, final String namespaceURI, final String publicId,
        final String systemId, final String baseURI) {
      log.debug("[RESOLVE]\n  type=" + type + "\n  namespaceURI=" + namespaceURI + "\n  publicId=" + publicId
          + "\n  systemId=" + systemId + "\n  baseURI=" + baseURI);
      return new XSDInput(type, namespaceURI, publicId, systemId, baseURI);
    }

  }

  private static class XSDInput implements LSInput {

    @SuppressWarnings("unused")
    private String type;
    @SuppressWarnings("unused")
    private String namespaceURI;
    private String publicId;
    private String systemId;
    private String baseURI;

    public XSDInput(final String type, final String namespaceURI, final String publicId, final String systemId,
        final String baseURI) {
      this.type = type;
      this.namespaceURI = namespaceURI;
      this.publicId = publicId;
      this.systemId = systemId;
      this.baseURI = baseURI;
    }

    @Override
    public Reader getCharacterStream() {
      log.debug("{get reader} this.systemId=" + this.systemId);
      InputStream is = this.getByteStream();
      return is == null ? null : new InputStreamReader(is);
    }

    @Override
    public void setCharacterStream(final Reader characterStream) {
      log.debug("{set reader} characterStream=" + characterStream);
      // Ignore
    }

    @Override
    public InputStream getByteStream() {
      log.debug("{getByteStream} this.systemId=" + this.systemId);
      InputStream is = ConfigurationLoader.class.getResourceAsStream(DEBUG_PATH + this.systemId);
      log.debug(DEBUG_PATH + this.systemId + " -> is=" + is);
      if (is == null) {
        is = ConfigurationLoader.class.getResourceAsStream(PLUGIN_PATH + this.systemId);
        log.debug(PLUGIN_PATH + this.systemId + " -> is=" + is);
      }
      return is;
    }

    @Override
    public void setByteStream(InputStream byteStream) {
      log.debug("{set bytestream} byteStream=" + byteStream);
      // Ignore
    }

    @Override
    public String getStringData() {
      log.debug("{ignore}");
      // Ignore
      return null;
    }

    @Override
    public void setStringData(String stringData) {
      log.debug("{set stringData} stringData=" + stringData);
      // Ignore
    }

    @Override
    public String getSystemId() {
      return this.systemId;
    }

    @Override
    public void setSystemId(String systemId) {
      log.debug("{ignore} systemId=" + systemId);
      // Ignore
    }

    @Override
    public String getPublicId() {
      return this.publicId;
    }

    @Override
    public void setPublicId(String publicId) {
      log.debug("{ignore} publicId=" + publicId);
      // Ignore
    }

    @Override
    public String getBaseURI() {
      return this.baseURI;
    }

    @Override
    public void setBaseURI(String baseURI) {
      log.debug("{ignore} baseURI=" + baseURI);
      // Ignore
    }

    @Override
    public String getEncoding() {
      log.debug("{ignore}");
      return null;
    }

    @Override
    public void setEncoding(String encoding) {
      log.debug("{ignore} encoding=" + encoding);
      // Ignore
    }

    @Override
    public boolean getCertifiedText() {
      log.debug("{ignore}");
      return false;
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
      log.debug("{ignore} certifiedText=" + certifiedText);
      // Ignore
    }

  }

}
