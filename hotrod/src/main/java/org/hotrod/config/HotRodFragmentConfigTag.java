package org.hotrod.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

import org.apache.commons.digester3.Digester;
import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.utils.ClassPackage;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HotRodFragmentConfigTag extends AbstractHotRodConfigTag {

  private static final Logger log = Logger.getLogger(HotRodFragmentConfigTag.class);

  private static final String TAG_NAME = "hotrod-fragment";

  private String sPackage = null;

  private String filename;

  private ClassPackage fragmentPackage;

  public HotRodFragmentConfigTag() {
    super(TAG_NAME);
  }

  public HotRodFragmentConfigTag load(final HotRodConfigTag config, final File f,
      final Set<String> alreadyLoadedFileNames, final File parentFile, final DaosTag daosTag)
      throws ControlledException, UncontrolledException {

    this.filename = f == null ? "<null>" : f.getPath();

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
      if (alreadyLoadedFileNames.contains(absPath)) {
        throw new ControlledException("Circular reference on " + Constants.TOOL_NAME
            + " configuration file. The configuration file " + absPath + "' cannot be loaded more than once, "
            + "but there's a reference on a <" + FragmentTag.TAG_NAME + "> tag of the file '"
            + (parentFile == null ? "N/A" : parentFile.getAbsolutePath()) + "' trying to load it again.");
      }
      alreadyLoadedFileNames.add(absPath);

      reader = new BufferedReader(new FileReader(f));

      // Define a XML digester and its rules

      Digester d = new Digester();

      addCommonRules(d, this.getClass());
      d.setEntityResolver(new HotRodFragmentInternalEntityResolver());

      this.addExtraRules(d);

      StrictValidationErrorHandler errorHandler = new StrictValidationErrorHandler();
      d.setErrorHandler(errorHandler);

      // Parse the configuration file

      log.debug("[ Will parse ]");
      HotRodFragmentConfigTag fragmentConfig = (HotRodFragmentConfigTag) d.parse(reader);
      log.debug("[ Parsed ]");

      if (!errorHandler.isValid()) {
        StringBuilder sb = new StringBuilder();
        sb.append("The " + Constants.TOOL_NAME + " configuration file '" + f.getAbsolutePath() + "' is not valid. "
            + "Please fix the following errors:\n");
        for (String msg : errorHandler.getMessages()) {
          sb.append("- " + msg + "\n");
        }
        throw new ControlledException(sb.toString());
      }

      // Validation (specific)

      fragmentConfig.validate(f.getParentFile());

      // Validation (common)

      fragmentConfig.validateCommon(config, f, alreadyLoadedFileNames, f, daosTag, fragmentConfig);

      // Complete

      return fragmentConfig;

    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException("Invalid configuration file '" + f.getAbsolutePath() + "': " + e.getMessage());

    } catch (SAXException e) {
      throw new ControlledException(
          "The configuration file '" + f.getAbsolutePath() + "' is not well-formed: " + e.getMessage());

    } catch (FileNotFoundException e) {
      throw new ControlledException("Configuration file '" + f.getAbsolutePath() + "' not found.");

    } catch (IOException e) {
      throw new UncontrolledException("Could not read the configuration file '" + f.getAbsolutePath(), e);

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

  // Setters (digester)

  public void setPackage(final String p) {
    this.sPackage = p;
  }

  // Getters

  public ClassPackage getFragmentPackage() {
    return fragmentPackage;
  }

  public String toString() {
    return "{fragment-config:" + this.filename + "}";
  }

  // Validation

  public void validate(final File basedir) throws InvalidConfigurationFileException {

    // package

    if (this.sPackage == null) {
      this.fragmentPackage = null;
    } else {
      try {
        this.fragmentPackage = new ClassPackage(this.sPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException("Invalid package '" + this.sPackage
            + "' on attribute 'package' of tag <" + TAG_NAME + ">: " + e.getMessage());
      }
    }

  }

  // Extra

  public void addExtraRules(final Digester d) {
    d.addSetProperties(super.base, "package", "package");
  }

  // Entity Resolver

  public static class HotRodFragmentInternalEntityResolver implements EntityResolver {

    private static final Logger log = Logger.getLogger(HotRodFragmentInternalEntityResolver.class);

    private static final String DTD = "/hotrod-fragment.dtd";

    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
      log.debug("*** public: " + publicId + " system: " + systemId);
      InputStream is = getClass().getResourceAsStream(DTD);
      log.debug("*** is=" + is);
      if (is == null) {
        throw new IOException("Could not find DTD definition file/resource: " + DTD);
      }
      return new InputSource(is);
    }

  }

}
