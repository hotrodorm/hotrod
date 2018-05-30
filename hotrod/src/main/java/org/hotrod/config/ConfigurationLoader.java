package org.hotrod.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.config.AbstractHotRodConfigTag.LocationListener;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.FileRegistry;
import org.hotrod.utils.FileRegistry.FileAlreadyRegisteredException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ConfigurationLoader {

  // Constants

  private static final Logger log = Logger.getLogger(ConfigurationLoader.class);

  // TODO: Improve the XSD schema assembling.
  // private static final String PRIMARY_XSD_PATH = "/hotrod.xsd";
  // private static final String FRAGMENT_XSD_PATH = "/hotrod-fragment.xsd";

  private static final String PRIMARY_XSD_PATH = "/src/main/xml/hotrod-primary-head.xsd";
  private static final String FRAGMENT_XSD_PATH = "/src/main/xml/hotrod-fragment-head.xsd";

  private static final String PLUGIN_PRIMARY_XSD_PATH = "/hotrod-primary-head.xsd";
  private static final String PLUGIN_FRAGMENT_XSD_PATH = "/hotrod-fragment-head.xsd";

  // Static properties

  private static Schema primarySchema = null;
  private static Schema fragmentSchema = null;

  // Behavior

  public static HotRodConfigTag loadPrimary(final File projectBaseDir, final File f, final String generatorName)
      throws ControlledException, UncontrolledException {

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
      FileInputStream xml = new FileInputStream(f);
      xsr = xif.createXMLStreamReader(xml);
      LocationListener locationListener = new LocationListener(f, xsr);
      unmarshaller.setListener(locationListener);
      log.debug("XML loaded.");
      
    } catch (SAXException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (JAXBException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (FileNotFoundException e) {
      throw new ControlledException(Constants.TOOL_NAME + " configuration file not found: " + f.getPath());
    } catch (XMLStreamException e) {
      String message = (e.getLocation() == null ? ""
          : "[line " + e.getLocation().getLineNumber() + ", col " + e.getLocation().getColumnNumber() + "] ")
          + e.getMessage();
      throw new ControlledException(
          Constants.TOOL_NAME + " configuration file '" + f.getPath() + "' is not well-formed: " + message);
    }

    // Parse the configuration file

    Reader reader = null;

    try {

      log.debug("[ Will parse ]");
      HotRodConfigTag config = (HotRodConfigTag) unmarshaller.unmarshal(xsr);
      log.debug("[ Parsed ]");

      // Validation (specific)

      log.debug("projectBaseDir=" + projectBaseDir + " :: " + projectBaseDir.getAbsolutePath());
      File parentDir = f.getParentFile();
      log.debug("parentFile=" + parentDir + " :: " + parentDir.getAbsolutePath());

      log.debug("Will validate semantics.");
      config.validate(projectBaseDir, parentDir, f, generatorName);
      log.debug("Semantics validation #1 successful.");

      // Validation (common)

      DaosTag daosTag = config.getGenerators().getSelectedGeneratorTag().getDaos();
      FileRegistry fileRegistry = new FileRegistry(f);

      config.validateCommon(config, f, fileRegistry, f, daosTag, null);
      log.debug("Semantics validation #2 successful.");

      config.addConverterTags();

      // Prepare transient values

      config.activate();

      // Complete

      log.debug("File loaded.");

      return config;

    } catch (JAXBException e) {
      throw assembleControlledException(f, validationHandler, e);

    } catch (InvalidConfigurationFileException e) {
      SourceLocation loc = e.getTag().getSourceLocation();
      if (loc == null) {
        throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage(),
            e.getInteractiveMessage());
      } else {
        throw new ControlledException(loc, e.getMessage(), e.getInteractiveMessage());
      }

    } catch (GeneratorNotFoundException e) {
      throw new ControlledException(e.getMessage());

    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          throw new UncontrolledException("Could not close configuration file.", e);
        }
      }
    }

  }

  public static HotRodFragmentConfigTag loadFragment(final HotRodConfigTag primaryConfig, final File f,
      final FileRegistry fileRegistry, final DaosTag daosTag, final FragmentTag fragmentTag)
      throws UncontrolledException, ControlledException {

    // Basic file validation

    if (f == null) {
      throw new ControlledException(fragmentTag.getSourceLocation(), "Configuration file name is empty.");
    }
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

    Reader reader = null;

    try {

      log.debug("[ *** Will parse file=" + f.getPath() + " ]");
      HotRodFragmentConfigTag fragmentConfig = (HotRodFragmentConfigTag) unmarshaller.unmarshal(xsr);
      log.debug("[ *** Parsed ]");

      // Validation (specific)

      fragmentConfig.validate(f.getParentFile());

      // Validation (common)

      fileRegistry.add(f);
      fragmentConfig.validateCommon(primaryConfig, f, fileRegistry, f, daosTag, fragmentConfig);

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
      throw new ControlledException(fragmentTag.getSourceLocation(),
          "Invalid configuration file '" + f.getPath() + "': this fragment file has already been loaded once.");
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          throw new UncontrolledException("Could not close configuration file.", e);
        }
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

  // Loading Grammar

  private static Schema getPrimarySchema() throws SAXException {
    if (primarySchema == null) {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      InputStream is = null;
      try {

        // checkExistence("/src/main/xml/hotrod-primary-head.xsd");
        // checkExistence("src/main/xml/hotrod-primary-head.xsd");
        // checkExistence("/src/main/xml/hotrod-fragment-head.xsd");
        // checkExistence("src/main/xml/hotrod-fragment-head.xsd");
        //
        // checkExistence("/hotrod-primary-head.xsd");
        // checkExistence("hotrod-primary-head.xsd");
        // checkExistence("/hotrod-fragment-head.xsd");
        // checkExistence("hotrod-fragment-head.xsd");

        is = ConfigurationLoader.class.getResourceAsStream(PRIMARY_XSD_PATH);
        if (is == null) { // try the plugin location
          is = ConfigurationLoader.class.getResourceAsStream(PLUGIN_PRIMARY_XSD_PATH);
        }
        primarySchema = factory.newSchema(new StreamSource(is));
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

  private static void checkExistence(final String name) {
    InputStream is = ConfigurationLoader.class.getResourceAsStream(name);
    log.info("--> Resource: " + name + " -- " + (is == null ? "does not exist" : "exists") + ".");
  }

  private static Schema getFragmentSchema() throws SAXException {
    if (fragmentSchema == null) {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      InputStream is = null;
      try {
        is = ConfigurationLoader.class.getResourceAsStream(FRAGMENT_XSD_PATH);
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

}
