package org.hotrod.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.digester3.Digester;
import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HotRodConfigTag extends AbstractHotRodConfigTag {

  private static final Logger log = Logger.getLogger(HotRodConfigTag.class);

  public HotRodConfigTag() {
    super("hotrod");
  }

  private GeneratorsTag generatorsTag = null;

  private List<ConverterTag> converters = new ArrayList<ConverterTag>();
  private Map<String, ConverterTag> convertersByName = null;

  public HotRodConfigTag load(final File f, final Set<String> alreadyLoadedFileNames, final File parentFile,
      final String generatorName) throws ControlledException, UncontrolledException {

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
      d.setEntityResolver(new HotRodInternalEntityResolver());

      this.addExtraRules(d);

      StrictValidationErrorHandler errorHandler = new StrictValidationErrorHandler();
      d.setErrorHandler(errorHandler);

      // Parse the configuration file

      log.debug("[ Will parse ]");
      HotRodConfigTag config = (HotRodConfigTag) d.parse(reader);
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

      config.validate(f.getParentFile(), generatorName);

      // Validation (common)

      DaosTag daosTag = config.getGenerators().getSelectedGeneratorTag().getDaos();

      config.validateCommon(config, f, alreadyLoadedFileNames, f, daosTag, null);

      // Complete

      return config;

    } catch (InvalidConfigurationFileException e) {
      throw new ControlledException("Invalid configuration file '" + f.getAbsolutePath() + "': " + e.getMessage());

    } catch (SAXException e) {
      throw new ControlledException(
          "The configuration file '" + f.getAbsolutePath() + "' is not well-formed: " + e.getMessage());

    } catch (FileNotFoundException e) {
      throw new ControlledException("Configuration file '" + f.getAbsolutePath() + "' not found.");

    } catch (IOException e) {
      throw new UncontrolledException("Could not read the configuration file '" + f.getAbsolutePath(), e);

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

  // Setters (digester)

  public void setGenerators(final GeneratorsTag generators) {
    this.generatorsTag = generators;
    log.debug("(1) this=" + this + " this.generatorsTag=" + this.generatorsTag);
  }

  public void addConverter(final ConverterTag converter) {
    this.converters.add(converter);
  }

  // Getters

  public GeneratorsTag getGenerators() {
    return generatorsTag;
  }

  public List<ConverterTag> getConverters() {
    return this.converters;
  }

  public ConverterTag getConverterTagByName(final String name) {
    return this.convertersByName.get(name);
  }

  public void addExtraRules(final Digester d) {

    // layout

    d.addObjectCreate(base + "/generators", GeneratorsTag.class);
    d.addSetNext(base + "/generators", "setGenerators");

    // mybatis

    d.addObjectCreate(base + "/generators/mybatis", MyBatisTag.class);
    d.addSetNext(base + "/generators/mybatis", "addGenerator");

    // daos

    d.addObjectCreate(base + "/generators/mybatis/daos", DaosTag.class);
    d.addSetProperties(base + "/generators/mybatis/daos", "gen-base-dir", "genBaseDir");
    d.addSetProperties(base + "/generators/mybatis/daos", "dao-package", "sdaoPackage");
    d.addSetProperties(base + "/generators/mybatis/daos", "primitives-package", "sprimitivesPackage");
    d.addSetProperties(base + "/generators/mybatis/daos", "dao-prefix", "daoPrefix");
    d.addSetProperties(base + "/generators/mybatis/daos", "dao-suffix", "daoSuffix");
    d.addSetProperties(base + "/generators/mybatis/daos", "primitives-prefix", "primitivesPrefix");
    d.addSetProperties(base + "/generators/mybatis/daos", "primitives-suffix", "primitivesSuffix");
    d.addSetNext(base + "/generators/mybatis/daos", "setDaos");

    // mappers

    d.addObjectCreate(base + "/generators/mybatis/mappers", MappersTag.class);
    d.addSetProperties(base + "/generators/mybatis/mappers", "gen-base-dir", "genBaseDir");
    d.addSetProperties(base + "/generators/mybatis/mappers", "relative-dir", "relativeDir");
    d.addSetNext(base + "/generators/mybatis/mappers", "setMappers");

    // mybatis-configuration-template

    d.addObjectCreate(base + "/generators/mybatis/mybatis-configuration-template", TemplateTag.class);
    d.addSetProperties(base + "/generators/mybatis/mybatis-configuration-template", "file", "templateFile");
    d.addSetNext(base + "/generators/mybatis/mybatis-configuration-template", "setTemplate");

    // session-factory

    d.addObjectCreate(base + "/generators/mybatis/session-factory", SessionFactoryTag.class);
    d.addSetProperties(base + "/generators/mybatis/session-factory", "singleton-full-class-name",
        "singletonFullClassName");
    d.addSetNext(base + "/generators/mybatis/session-factory", "setSessionFactory");

    // select-generation

    d.addObjectCreate(base + "/generators/mybatis/select-generation", SelectGenerationTag.class);
    d.addSetProperties(base + "/generators/mybatis/select-generation", "temp-view-base-name", "tempViewBaseName");
    d.addSetNext(base + "/generators/mybatis/select-generation", "setSelectGeneration");

    // property

    d.addObjectCreate(base + "/generators/mybatis/property", PropertyTag.class);
    d.addSetProperties(base + "/generators/mybatis/property", "name", "name");
    d.addSetProperties(base + "/generators/mybatis/property", "value", "value");
    d.addSetNext(base + "/generators/mybatis/property", "addProperty");

    // spring-jdbc

    d.addObjectCreate(base + "/generators/spring-jdbc", SpringJDBCTag.class);
    d.addSetNext(base + "/generators/spring-jdbc", "addGenerator");

    // daos

    d.addObjectCreate(base + "/generators/spring-jdbc/daos", DaosTag.class);
    d.addSetProperties(base + "/generators/spring-jdbc/daos", "gen-base-dir", "genBaseDir");
    d.addSetProperties(base + "/generators/spring-jdbc/daos", "dao-package", "sdaoPackage");
    d.addSetProperties(base + "/generators/spring-jdbc/daos", "dao-prefix", "daoPrefix");
    d.addSetProperties(base + "/generators/spring-jdbc/daos", "dao-suffix", "daoSuffix");
    d.addSetProperties(base + "/generators/spring-jdbc/daos", "primitives-prefix", "primitivesPrefix");
    d.addSetProperties(base + "/generators/spring-jdbc/daos", "primitives-suffix", "primitivesSuffix");
    d.addSetNext(base + "/generators/spring-jdbc/daos", "setDaos");

    // config

    d.addObjectCreate(base + "/generators/spring-jdbc/config", ConfigTag.class);
    d.addSetProperties(base + "/generators/spring-jdbc/config", "gen-base-dir", "sgenBaseDir");
    d.addSetProperties(base + "/generators/spring-jdbc/config", "relative-dir", "relativeDir");
    d.addSetProperties(base + "/generators/spring-jdbc/config", "prefix", "prefix");
    d.addSetNext(base + "/generators/spring-jdbc/config", "setConfig");

    // select-generation

    d.addObjectCreate(base + "/generators/spring-jdbc/select-generation", SelectGenerationTag.class);
    d.addSetProperties(base + "/generators/spring-jdbc/select-generation", "temp-view-base-name", "tempViewBaseName");
    d.addSetNext(base + "/generators/spring-jdbc/select-generation", "setSelectGeneration");

  }

  public void validate(final File basedir, final String generatorName)
      throws InvalidConfigurationFileException, GeneratorNotFoundException {

    // Generators

    this.generatorsTag.validate(basedir, generatorName);

    // Converters

    this.convertersByName = new HashMap<String, ConverterTag>();

    for (ConverterTag c : this.converters) {
      c.validate();
      if (this.convertersByName.containsKey(c.getName())) {
        throw new InvalidConfigurationFileException(
            "Duplicate converter name. There are multiple <converter> tags with the same name: '" + c.getName() + "'.");
      }
      this.convertersByName.put(c.getName(), c);
    }

  }

  // Entity Resolver

  public static class HotRodInternalEntityResolver implements EntityResolver {

    private static final Logger log = Logger.getLogger(HotRodInternalEntityResolver.class);

    private static final String DTD = "/hotrod.dtd";

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
