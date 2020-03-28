package org.hotrod.eclipseplugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.hotrod.eclipseplugin.utils.ClassPathEncoder;
import org.hotrod.eclipseplugin.utils.SerialUtils;
import org.hotrod.generator.CachedMetadata;

public class FileProperties {

  // Constants

  private static final Logger log = Logger.getLogger(FileProperties.class);

  public static final String CONFIGURED_ATT = "configured";
  public static final String DRIVERCLASSPATH_ATT = "driverclasspath";
  public static final String DRIVERCLASSNAME_ATT = "driverclassname";
  public static final String URL_ATT = "url";
  public static final String USERNAME_ATT = "username";
  public static final String PASSWORD_ATT = "password";
  public static final String CATALOG_ATT = "catalog";
  public static final String SCHEMA_ATT = "schema";
  public static final String GENERATOR_ATT = "generator";

  private static final String CONFIGURED_FALSE = "0";
  private static final String CONFIGURED_TRUE = "1";

  // Properties

  private ProjectProperties projectProperties;
  private String code;
  private String relativeFileName;

  private boolean configured;
  private List<String> driverClassPathEntries;
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private String catalog;
  private String schema;
  private String generator;

  private CachedMetadata cachedMetadata;

  // Constructor

  public FileProperties(final ProjectProperties projectProperties, final String code, final String relativeFileName) {
    log.debug("init");
    this.projectProperties = projectProperties;
    this.code = code;
    this.relativeFileName = relativeFileName;

    this.configured = false;
    this.driverClassPathEntries = new ArrayList<String>();
    this.driverClassName = "";
    this.url = "";
    this.username = "";
    this.password = "";
    this.catalog = "";
    this.schema = "";
    this.generator = "";

    this.cachedMetadata = new CachedMetadata();
  }

  // Persistence

  public static FileProperties load(final ProjectProperties projectProperties, final String code,
      final String relativeFileName) throws CouldNotLoadFilePropertiesException {
    FileProperties fileProperties = new FileProperties(projectProperties, code, relativeFileName);
    IPath pp = projectProperties.getProjectDir().append(getFileName(code));
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(pp.toFile()));
      for (String name : prop.stringPropertyNames()) {
        String value = prop.getProperty(name);
        fileProperties.applyProperty(name, value);
      }
      fileProperties.readCache();
      return fileProperties;
    } catch (FileNotFoundException e) {
      return fileProperties;
    } catch (IOException e) {
      throw new CouldNotLoadFilePropertiesException(e.getMessage());
    } catch (CouldNotLoadFilePropertiesException e) {
      throw new CouldNotLoadFilePropertiesException(e.getMessage());
    } catch (Throwable e) {
      throw new CouldNotLoadFilePropertiesException(e.getMessage());
    }
  }

  private static String getFileName(final String code) {
    return code + ".properties";
  }

  private void applyProperty(final String name, final String value) throws CouldNotLoadFilePropertiesException {
    if (CONFIGURED_ATT.equals(name)) {
      this.configured = CONFIGURED_TRUE.equals(value);
    } else if (DRIVERCLASSPATH_ATT.equals(name)) {
      this.driverClassPathEntries = ClassPathEncoder.decode(value);
    } else if (DRIVERCLASSNAME_ATT.equals(name)) {
      this.driverClassName = value;
    } else if (URL_ATT.equals(name)) {
      this.url = value;
    } else if (USERNAME_ATT.equals(name)) {
      this.username = value;
    } else if (PASSWORD_ATT.equals(name)) {
      this.password = value;
    } else if (CATALOG_ATT.equals(name)) {
      this.catalog = value;
    } else if (SCHEMA_ATT.equals(name)) {
      this.schema = value;
    } else if (GENERATOR_ATT.equals(name)) {
      this.generator = value;
    } else {
      throw new CouldNotLoadFilePropertiesException("Unrecognized property name '" + name + "'.");
    }
  }

  private static String getCacheFileName(final String code) {
    return code + ".cache";
  }

  public void save() throws CouldNotSaveFilePropertiesException {
    IPath pp = this.projectProperties.getProjectDir().append(getFileName(this.code));

    Properties prop = new Properties();
    prop.put(CONFIGURED_ATT, this.configured ? CONFIGURED_TRUE : CONFIGURED_FALSE);
    prop.put(DRIVERCLASSPATH_ATT, ClassPathEncoder.encode(this.driverClassPathEntries));
    prop.put(DRIVERCLASSNAME_ATT, this.driverClassName);
    prop.put(URL_ATT, this.url);
    prop.put(USERNAME_ATT, this.username);
    prop.put(PASSWORD_ATT, this.password);
    prop.put(CATALOG_ATT, this.catalog);
    prop.put(SCHEMA_ATT, this.schema);
    prop.put(GENERATOR_ATT, this.generator);

    try {
      prop.store(new FileOutputStream(pp.toFile()), "HotRod file properties\n");
      writeCache();
    } catch (FileNotFoundException e) {
      throw new CouldNotSaveFilePropertiesException(e.getMessage());
    } catch (IOException e) {
      throw new CouldNotSaveFilePropertiesException(e.getMessage());
    }
  }

  public static void remove(final ProjectProperties projectProperties, final String code) {

    // Leave the properties file on disk

    // IPath pp = projectProperties.getProjectDir().append(getFileName(code));
    // pp.toFile().delete();

    // Always remove the cache file

    IPath cp = projectProperties.getProjectDir().append(getCacheFileName(code));
    cp.toFile().delete();

  }

  private void readCache() throws CouldNotLoadFilePropertiesException {
    IPath pc = this.projectProperties.getProjectDir().append(getCacheFileName(this.code));
    if (pc.toFile().exists()) {
      try {
        this.cachedMetadata = (CachedMetadata) SerialUtils
            .deserialize(new GZIPInputStream(new FileInputStream(pc.toFile())));
      } catch (FileNotFoundException e) {
        throw new CouldNotLoadFilePropertiesException("Could not read cache for file '" + this.relativeFileName + "'.");
      } catch (IOException e) {
        throw new CouldNotLoadFilePropertiesException("Could not read cache for file '" + this.relativeFileName + "'.");
      } catch (ClassNotFoundException e) {
        throw new CouldNotLoadFilePropertiesException("Could not read cache for file '" + this.relativeFileName + "'.");
      }
    }
  }

  private void writeCache() throws CouldNotSaveFilePropertiesException {
    IPath pc = this.projectProperties.getProjectDir().append(getCacheFileName(this.code));
    try {
      SerialUtils.serialize(this.cachedMetadata, new GZIPOutputStream(new FileOutputStream(pc.toFile())));
    } catch (FileNotFoundException e) {
      log.error("Could not save cache for file '" + this.relativeFileName + "'.", e);
      throw new CouldNotSaveFilePropertiesException("Could not save cache for file '" + this.relativeFileName + "'.");
    } catch (IOException e) {
      log.error("Could not save cache for file '" + this.relativeFileName + "'.", e);
      throw new CouldNotSaveFilePropertiesException("Could not save cache for file '" + this.relativeFileName + "'.");
    }
  }

  // Getters & Setters

  public List<String> getDriverClassPathEntries() {
    return driverClassPathEntries;
  }

  public void setDriverClassPathEntries(List<String> driverClassPathEntries) {
    this.driverClassPathEntries = driverClassPathEntries;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getGenerator() {
    return generator;
  }

  public void setGenerator(String generator) {
    this.generator = generator;
  }

  public CachedMetadata getCachedMetadata() {
    return cachedMetadata;
  }

  public boolean isConfigured() {
    return configured;
  }

  public void setConfigured(boolean configured) {
    this.configured = configured;
  }

  // Exceptions

  public static class CouldNotLoadFilePropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotLoadFilePropertiesException(final String message) {
      super(message);
    }

  }

  public static class CouldNotSaveFilePropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotSaveFilePropertiesException(final String message) {
      super(message);
    }

  }

}
