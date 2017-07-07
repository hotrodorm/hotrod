package org.hotrod.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.xml.sax.SAXException;

public class ConfigurationLoader {

  // Constants

  private static final Logger log = Logger.getLogger(ConfigurationLoader.class);

  private static final String PRIMARY_XSD_PATH = "/hotrod.xsd";
  private static final String FRAGMENT_XSD_PATH = "/hotrod-fragment.xsd";

  // Static properties

  private static Schema primarySchema = null;
  private static Schema fragmentSchema = null;

  // Behavior

  public static HotRodConfigTag loadPrimary(final File f, final String generatorName)
      throws ControlledException, UncontrolledException {

    // Prepare the parser

    Unmarshaller unmarshaller = null;
    StrictValidationEventHandler validationHandler = new StrictValidationEventHandler();

    try {
      Schema schema = getPrimarySchema();

      JAXBContext context = JAXBContext.newInstance(HotRodConfigTag.class);

      unmarshaller = context.createUnmarshaller();
      unmarshaller.setEventHandler(validationHandler);
      unmarshaller.setSchema(schema);

    } catch (SAXException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (JAXBException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    }

    // Parse the configuration file

    Reader reader = null;

    try {

      if (f == null) {
        throw new ControlledException("Configuration file name is empty.");
      }
      if (!f.exists()) {
        throw new ControlledException(Constants.TOOL_NAME + " configuration file not found: " + f.getAbsolutePath());
      }
      if (!f.isFile()) {
        throw new ControlledException(Constants.TOOL_NAME + " configuration file '" + f.getAbsolutePath()
            + "' must be a normal file, not a directory or other special file.");
      }
      String absPath = f.getAbsolutePath();

      log.debug("[ Will parse ]");
      HotRodConfigTag config = (HotRodConfigTag) unmarshaller.unmarshal(f);
      log.debug("[ Parsed ]");

      // Validation (specific)

      config.validate(f.getParentFile(), generatorName);

      // Validation (common)

      DaosTag daosTag = config.getGenerators().getSelectedGeneratorTag().getDaos();

      Set<String> alreadyLoadedFileNames = new HashSet<String>();
      alreadyLoadedFileNames.add(absPath);

      config.validateCommon(config, f, alreadyLoadedFileNames, f, daosTag, null);

      // Complete

      return config;

    } catch (JAXBException e) {
      throw new ControlledException("The configuration file '" + f.getPath() + "' is not valid. An error was found at "
          + validationHandler.getLocation() + ":\n" + e.getMessage());

    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());

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

    // Prepare the parser

    Unmarshaller unmarshaller = null;
    StrictValidationEventHandler validationHandler = new StrictValidationEventHandler();

    try {
      Schema schema = getFragmentSchema();

      JAXBContext context = JAXBContext.newInstance(HotRodFragmentConfigTag.class);

      unmarshaller = context.createUnmarshaller();
      unmarshaller.setEventHandler(validationHandler);
      unmarshaller.setSchema(schema);

    } catch (SAXException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    } catch (JAXBException e) {
      throw new UncontrolledException("Could not load configuration file [internal XML parser error]", e);
    }

    // Parse the configuration file

    Reader reader = null;

    try {

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
      String absPath = f.getAbsolutePath();

      log.debug("[ *** Will parse file=" + f.getPath() + " ]");
      HotRodFragmentConfigTag fragmentConfig = (HotRodFragmentConfigTag) unmarshaller.unmarshal(f);
      log.debug("[ *** Parsed ]");

      // Validation (specific)

      fragmentConfig.validate(f.getParentFile());

      // Validation (common)

      alreadyLoadedFileNames.add(absPath);
      fragmentConfig.validateCommon(primaryConfig, f, alreadyLoadedFileNames, f, daosTag, fragmentConfig);

      // Complete

      return fragmentConfig;

    } catch (JAXBException e) {
      throw new ControlledException("The configuration file '" + f.getPath() + "' is not valid. An error was found at "
          + validationHandler.getLocation() + ":\n" + e.getMessage());

    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException("Invalid configuration file '" + f.getPath() + "': " + e.getMessage());

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
