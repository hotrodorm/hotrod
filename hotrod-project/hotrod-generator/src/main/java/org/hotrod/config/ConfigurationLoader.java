package org.hotrod.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashSet;

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
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.FileRegistry;
import org.hotrod.utils.FileRegistry.FileAlreadyRegisteredException;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ConfigurationLoader {

  // private static final String DEBUG_PATH = "/src/main/xml/";
  private static final String DEBUG_PATH = "/";
  private static final String PLUGIN_PATH = "/xml/";

  // Constants

  private static final Logger log = LogManager.getLogger(ConfigurationLoader.class);

  private static final String DEBUG_PRIMARY_XSD_PATH = DEBUG_PATH + "hotrod.xsd";
  private static final String DEBUG_FRAGMENT_XSD_PATH = DEBUG_PATH + "hotrod-fragment.xsd";

  private static final String PLUGIN_PRIMARY_XSD_PATH = PLUGIN_PATH + "hotrod-primary-head.xsd";
  private static final String PLUGIN_FRAGMENT_XSD_PATH = PLUGIN_PATH + "hotrod-fragment-head.xsd";

  // Static properties

  private static Schema primarySchema = null;
  private static Schema fragmentSchema = null;

  // Behavior

  public static HotRodConfigTag loadPrimary(final File projectBaseDir, final File f, final DatabaseAdapter adapter,
      final LinkedHashSet<String> facetNames, final CatalogSchema currentCS)
      throws ControlledException, UncontrolledException, FacetNotFoundException {

    log.debug("loading file: " + f);

    // Basic validation on the file

    if (f == null) {
      throw new ControlledException("Configuration file name is empty.");
    }
    if (!f.exists()) {
      throw new ControlledException(Constants.TOOL_NAME + " configuration file not found: " + f.getPath());
    }
    if (!f.isFile()) {
      throw new ControlledException(Constants.TOOL_NAME + " configuration file '" + f.getPath()
          + "' must be a normal file, not a directory or other special file.");
    }

    // Prepare the parser

    log.debug("loading file 2");

    Unmarshaller unmarshaller = null;
    XMLStreamReader xsr = null;
    StrictValidationEventHandler validationHandler = new StrictValidationEventHandler();

    try {
      Schema schema = getPrimarySchema();

      JAXBContext context = JAXBContext.newInstance(HotRodConfigTag.class);

      unmarshaller = context.createUnmarshaller();
      unmarshaller.setEventHandler(validationHandler);
      unmarshaller.setSchema(schema);

      XMLInputFactory xif = XMLInputFactory.newFactory();
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

      FileInputStream xml = new FileInputStream(f);
      xsr = xif.createXMLStreamReader(xml);
      LocationListener locationListener = new LocationListener(f, xsr);
      unmarshaller.setListener(locationListener);
      log.debug("XML loaded.");

    } catch (SAXException e) {
      e.printStackTrace();
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (JAXBException e) {
      e.printStackTrace();
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new ControlledException(Constants.TOOL_NAME + " configuration file not found: " + f.getPath());
    } catch (XMLStreamException e) {
      e.printStackTrace();
      String message = (e.getLocation() == null ? ""
          : "[line " + e.getLocation().getLineNumber() + ", col " + e.getLocation().getColumnNumber() + "] ")
          + e.getMessage();
      throw new ControlledException(
          Constants.TOOL_NAME + " configuration file '" + f.getPath() + "' is not well-formed: " + message);
    }

    // Parse the configuration file

    try {

      log.debug("[ Will parse ]");
      HotRodConfigTag config = (HotRodConfigTag) unmarshaller.unmarshal(xsr);
      log.debug("[ Parsed ]");

      // Validation (specific)

      log.debug("projectBaseDir=" + projectBaseDir + " :: " + projectBaseDir.getAbsolutePath());
      File parentDir = f.getParentFile();
      log.debug("parentFile=" + parentDir + " :: " + parentDir.getAbsolutePath());

      log.debug("Will validate semantics.");
      config.validate(projectBaseDir, parentDir, f, adapter, currentCS);
      log.debug("Semantics validation #1 successful.");

      // Validation (common)

      DaosTag daosTag = (DaosTag) config.getGenerators().getSelectedGeneratorTag().getDaos();
      FileRegistry fileRegistry = new FileRegistry(f);

      config.validateCommon(config, f, fileRegistry, f, daosTag, null, adapter, facetNames, currentCS);
      log.debug("Semantics validation #2 successful.");

      config.addConverterTags();

      MyBatisSpringTag mst = (MyBatisSpringTag) config.getGenerators().getSelectedGeneratorTag();
      if (mst.getDiscover() != null && !config.getFacets().isEmpty()) {
        throw new ControlledException(config.getSourceLocation(),
            "The <discover> tag is mutually exclusive with <facet> tags. "
                + "If you want to use discover you cannot use facets, and vice versa.");
      }

      // Prepare transient values

      config.activate();

      // Complete

      log.debug("File loaded.");

      return config;

    } catch (JAXBException e) {
      log.debug("JAXBException", e);
      throw assembleControlledException(f, validationHandler, e);

    } catch (InvalidConfigurationFileException e) {
      log.debug("InvalidConfigurationFileException", e);
      SourceLocation loc = e.getTag().getSourceLocation();
      log.debug("loc=" + loc);
      if (loc == null) {
        throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());
      } else {
        throw new ControlledException(loc, e.getMessage());
      }

    } catch (GeneratorNotFoundException e) {
      log.debug("GeneratorNotFoundException.", e);
      throw new ControlledException(e.getMessage());

    } catch (Throwable e) {
      log.debug("Throwable detected.", e);
      throw new ControlledException(e.getMessage());

    }

  }

  // When no hotrod.xml is provided

  public static HotRodConfigTag prepareNoConfig(final File projectBaseDir, final File f, final DatabaseAdapter adapter,
      final LinkedHashSet<String> facetNames, final CatalogSchema currentCS) throws ControlledException {
    HotRodConfigTag config = new HotRodConfigTag();
    File parentDir = null;
    try {
      config.validate(projectBaseDir, parentDir, f, adapter, currentCS);
    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException("No Config Error: " + e.getMessage());
    } catch (GeneratorNotFoundException e) {
      throw new ControlledException("No Config Error: " + e.getMessage());
    }
    DaosTag daosTag = (DaosTag) config.getGenerators().getSelectedGeneratorTag().getDaos();
    FileRegistry fileRegistry = new FileRegistry(f);
    try {
      config.validateCommon(config, f, fileRegistry, f, daosTag, null, adapter, facetNames, currentCS);
    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException("No Config Error: " + e.getMessage());
    } catch (UncontrolledException e) {
      throw new ControlledException("No Config Error: " + e.getMessage());
    } catch (FacetNotFoundException e) {
      throw new ControlledException("No Config Error: " + e.getMessage());
    }
    return config;
  }

  public static HotRodFragmentConfigTag loadFragment(final HotRodConfigTag primaryConfig, final File f,
      final FileRegistry fileRegistry, final DaosTag daosTag, final FragmentTag fragmentTag,
      final DatabaseAdapter adapter, final LinkedHashSet<String> facetNames, final CatalogSchema currentCS)
      throws UncontrolledException, ControlledException, FacetNotFoundException {

    // Basic file validation

    if (f == null) {
      throw new ControlledException(fragmentTag.getSourceLocation(), "Configuration file name is empty.");
    }
    log.debug("-- loading fragment: " + f.getName());
    if (!f.exists()) {
      throw new ControlledException(fragmentTag.getSourceLocation(),
          Constants.TOOL_NAME + " configuration file not found: " + f.getPath());
    }
    if (!f.isFile()) {
      throw new ControlledException(fragmentTag.getSourceLocation(), Constants.TOOL_NAME + " configuration file '"
          + f.getPath() + "' must be a normal file, not a directory or other special file.");
    }

    // Prepare the parser

    Unmarshaller unmarshaller = null;
    XMLStreamReader xsr = null;
    StrictValidationEventHandler validationHandler = new StrictValidationEventHandler();

    try {
      Schema schema = getFragmentSchema();

      JAXBContext context = JAXBContext.newInstance(HotRodFragmentConfigTag.class);

      unmarshaller = context.createUnmarshaller();
      unmarshaller.setEventHandler(validationHandler);
      unmarshaller.setSchema(schema);

      XMLInputFactory xif = XMLInputFactory.newFactory();
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

      FileInputStream xml = new FileInputStream(f);
      xsr = xif.createXMLStreamReader(xml);
      LocationListener locationListener = new LocationListener(f, xsr);
      unmarshaller.setListener(locationListener);

    } catch (SAXException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (JAXBException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (FileNotFoundException e) {
      throw new ControlledException(fragmentTag.getSourceLocation(),
          Constants.TOOL_NAME + " configuration file not found: " + f.getPath());
    } catch (XMLStreamException e) {
      String message = (e.getLocation() == null ? ""
          : "[line " + e.getLocation().getLineNumber() + ", col " + e.getLocation().getColumnNumber() + "] ")
          + e.getMessage();
      throw new ControlledException(fragmentTag.getSourceLocation(),
          Constants.TOOL_NAME + " configuration file '" + f.getPath() + "' is not well-formed: " + message);
    }

    // Parse the configuration file

    try {

      log.debug("[ *** Will parse file=" + f.getPath() + " ]");
      HotRodFragmentConfigTag fragmentConfig = (HotRodFragmentConfigTag) unmarshaller.unmarshal(xsr);
      log.debug("[ *** Parsed ]");

      // Validation (specific)

      fragmentConfig.validate(f.getParentFile());

      // Validation (common)

      log.debug("--       Registering f=" + f);
      log.debug("  --     tag: " + fragmentTag.getSourceLocation());
      fileRegistry.add(fragmentTag, f);
      log.debug("----2> fileRegistry=" + fileRegistry);
      fragmentConfig.validateCommon(primaryConfig, f, fileRegistry, f, daosTag, fragmentConfig, adapter, facetNames,
          currentCS);

      // Complete

      return fragmentConfig;

    } catch (JAXBException e) {
      throw assembleControlledException(f, validationHandler, e);

    } catch (InvalidConfigurationFileException e) {
      SourceLocation loc = e.getTag().getSourceLocation();
      if (loc == null) {
        throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());
      } else {
        throw new ControlledException(loc, e.getMessage());
      }

    } catch (FileAlreadyRegisteredException e) {
      log.debug("********** exception in tag: " + e.getContainerTag().getSourceLocation());
      throw new ControlledException(e.getContainerTag().getSourceLocation(),
          "Invalid configuration file '" + f.getPath() + "': this fragment file has already been loaded once.");
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

  // Loading Grammar

  private static Schema getPrimarySchema() throws SAXException {
    if (primarySchema == null) {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      LSResourceResolver xsdResolver = new XSDResolver();
      factory.setResourceResolver(xsdResolver);

      InputStream is = null;
      try {

        is = ConfigurationLoader.class.getResourceAsStream(DEBUG_PRIMARY_XSD_PATH);
        log.debug("[Load #1] is=" + is);

        if (is == null) { // try the plugin location
          log.debug("[Load #2]");
          is = ConfigurationLoader.class.getResourceAsStream(PLUGIN_PRIMARY_XSD_PATH);
          log.debug("[Load #3] is=" + is);
        }
        log.debug("[Load #4]");
        primarySchema = factory.newSchema(new StreamSource(is));
        log.debug("[Load #5] primarySchema =" + primarySchema);

      } catch (SAXException e) {
//        e.printStackTrace();
        log.error("Failed to load", e);
        throw e;
      } catch (RuntimeException e) {
        log.error("(RuntimeException) Failed to load", e);
      } finally {
        if (is != null) {
          try {
            is.close();
          } catch (IOException e) {
            log.error("Could not close primary schema file.");
          }
        }
      }
    }
    return primarySchema;
  }

  private static Schema getFragmentSchema() throws SAXException {
    if (fragmentSchema == null) {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      LSResourceResolver xsdResolver = new XSDResolver();
      factory.setResourceResolver(xsdResolver);

      InputStream is = null;
      try {
        is = ConfigurationLoader.class.getResourceAsStream(DEBUG_FRAGMENT_XSD_PATH);
        if (is == null) { // try the plugin location
          is = ConfigurationLoader.class.getResourceAsStream(PLUGIN_FRAGMENT_XSD_PATH);
        }
        fragmentSchema = factory.newSchema(new StreamSource(is));
      } finally {
        if (is != null) {
          try {
            is.close();
          } catch (IOException e) {
            log.error("Could not close fragment schema file.");
          }
        }
      }
    }
    return fragmentSchema;
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
