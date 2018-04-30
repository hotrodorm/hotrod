package org.hotrod.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

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
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractHotRodConfigTag.LocationListener;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.dynamicsql.SourceLocation;
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

  // Static properties

  private static Schema primarySchema = null;
  private static Schema fragmentSchema = null;

  // Behavior

  public static HotRodConfigTag loadPrimary(final File projectBaseDir, final File f, final String generatorName)
      throws ControlledException, UncontrolledException {

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

      config.validate(projectBaseDir, parentDir, generatorName);

      // Validation (common)

      DaosTag daosTag = config.getGenerators().getSelectedGeneratorTag().getDaos();

      Set<String> alreadyLoadedFileNames = new HashSet<String>();
      alreadyLoadedFileNames.add(f.getAbsolutePath());

      config.validateCommon(config, f, alreadyLoadedFileNames, f, daosTag, null);

      // Prepare transient values

      config.activate();

      // Complete

      return config;

    } catch (JAXBException e) {
      throw assembleControlledException(f, validationHandler, e);

    } catch (InvalidConfigurationFileException e) {
      SourceLocation loc = e.getSourceLocation();
      if (loc == null) {
        throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());
      } else {
        throw new ControlledException(loc, e.getMessage());
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
      final File parentFile, final Set<String> alreadyLoadedFileNames, final DaosTag daosTag)
      throws UncontrolledException, ControlledException {

    // Basic file validation

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

      log.debug("[ *** Will parse file=" + f.getPath() + " ]");
      HotRodFragmentConfigTag fragmentConfig = (HotRodFragmentConfigTag) unmarshaller.unmarshal(xsr);
      log.debug("[ *** Parsed ]");

      // Validation (specific)

      fragmentConfig.validate(f.getParentFile());

      // Validation (common)

      alreadyLoadedFileNames.add(f.getAbsolutePath());
      fragmentConfig.validateCommon(primaryConfig, f, alreadyLoadedFileNames, f, daosTag, fragmentConfig);

      // Complete

      return fragmentConfig;

    } catch (JAXBException e) {
      throw assembleControlledException(f, validationHandler, e);

    } catch (InvalidConfigurationFileException e) {
      SourceLocation loc = e.getSourceLocation();
      if (loc == null) {
        throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());
      } else {
        throw new ControlledException(loc, e.getMessage());
      }

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
        is = ConfigurationLoader.class.getResourceAsStream(PRIMARY_XSD_PATH);
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

  private static Schema getFragmentSchema() throws SAXException {
    if (fragmentSchema == null) {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      InputStream is = null;
      try {
        is = ConfigurationLoader.class.getResourceAsStream(FRAGMENT_XSD_PATH);
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
