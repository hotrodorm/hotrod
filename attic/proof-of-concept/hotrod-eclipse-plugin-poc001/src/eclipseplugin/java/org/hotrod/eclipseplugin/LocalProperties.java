package org.hotrod.eclipseplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.hotrod.domain.MainConfigFile;
import org.hotrod.eclipseplugin.utils.ClassPathEncoder;
import org.hotrod.eclipseplugin.utils.ObjectPropertyCodec;
import org.hotrod.eclipseplugin.utils.ObjectPropertyCodec.CouldNotDecodeException;
import org.hotrod.eclipseplugin.utils.ObjectPropertyCodec.CouldNotEncodeException;
import org.hotrod.eclipseplugin.utils.SUtil;

/**
 * <pre>
 *                                             (save)
 *   local config cache <--------------------------------------------------------+
 *          |                                                                    |
 *          V                                                                    |
 *    [DRAG & DROP LOAD]                                                         |
 *       |     |                               (update)                          |
 *       |     +-> "cached" config <------------------------------------------+  |
 *       |                \                                                   |  |
 *       |                 \                                                  |  |
 *       |                  *-> "correlated" config --> Eclipse Plugin --> [GENERATE]
 *       |                 /                   \
 *       |                /                     \
 *       +-----> "file" config                   *--> ...another client...
 *                    ^
 *                    |
 *           [FILE CHANGE DETECTED]
 *                    |
 *                config file
 *
 * </pre>
 */
public class LocalProperties {

  public static final String CONFIG_FILE_NAME = "hotrod.local.properties";

  public static final String FORMAT_ATT = "format";
  public static final String CURRENT_FORMAT = "F1";

  public static final String FILENAME_ATT = "filename";
  public static final String DRIVERCLASSPATH_ATT = "driverclasspath";
  public static final String DRIVERCLASSNAME_ATT = "driverclassname";
  public static final String URL_ATT = "url";
  public static final String USERNAME_ATT = "username";
  public static final String PASSWORD_ATT = "password";
  public static final String CATALOG_ATT = "catalog";
  public static final String SCHEMA_ATT = "schema";
  public static final String GENERATOR_ATT = "generator";

  public static final String CACHE_PREFIX_ATT = "cache-";

  private IProject project;
  private String format;
  private TreeMap<String, FileProperties> files;

  public LocalProperties(final IProject project) {
    this.project = project;
    this.files = new TreeMap<String, FileProperties>();
  }

  private LocalProperties(final IProject project, final String format, final TreeMap<String, FileProperties> files) {
    this.project = project;
    this.format = format;
    this.files = files;
  }

  // Getters

  public FileProperties getFileProperties(final String fileName) {
    log("[X2] this.files.size()=" + this.files.size());
    return this.files.get(fileName);
  }

  public void addFileProperties(final String fileName, final FileProperties fileProperties) {
    this.files.put(fileName, fileProperties);
  }

  // Persistence

  public static LocalProperties load(final IProject project) throws CouldNotLoadPropertiesException {
    File f = new File(project.getLocation().toFile(), CONFIG_FILE_NAME);

    String format = null;

    if (f.exists()) {
      BufferedReader r = null;
      try {
        r = new BufferedReader(new FileReader(f));
        Properties p = new Properties();
        p.load(r);

        // 1. Assemble file properties

        Map<String, FileProperties> properties = new TreeMap<String, FileProperties>();

        for (String name : p.stringPropertyNames()) {
          String value = p.getProperty(name);

          if (FORMAT_ATT.equals(name)) {

            format = value;

          } else {

            int dot = name.indexOf('.');
            if (dot == -1) {
              throw new CouldNotLoadPropertiesException(
                  "Invalid property name '" + name + "'. A property name must have the form 'file.attribute'.");
            }

            String file = name.substring(0, dot).trim();
            if (file.isEmpty()) {
              throw new CouldNotLoadPropertiesException(
                  "Invalid property name '" + name + "'. A property name must have the form 'file.attribute'.");
            }

            String attribute = name.substring(dot + 1).trim();
            if (attribute.isEmpty()) {
              throw new CouldNotLoadPropertiesException(
                  "Invalid property name '" + name + "'. A property name must have the form 'file.attribute'.");
            }

            FileProperties fileProperties = properties.get(file);
            if (fileProperties == null) {
              fileProperties = new FileProperties("");
              properties.put(file, fileProperties);
            }
            fileProperties.set(name, attribute, value);

          }

        }

        // 2. Validate each file & assemble collection

        TreeMap<String, FileProperties> files = new TreeMap<String, FileProperties>();
        for (String file : properties.keySet()) {
          FileProperties fileProperties = properties.get(file);
          fileProperties.validate(file);
          files.put(fileProperties.fileName, fileProperties);
        }

        return new LocalProperties(project, CURRENT_FORMAT, files);

      } catch (FileNotFoundException e) {
        throw new CouldNotLoadPropertiesException(e.getMessage());
      } catch (IOException e) {
        throw new CouldNotLoadPropertiesException(e.getMessage());
      } finally {
        if (r != null) {
          try {
            r.close();
          } catch (IOException e) {
            // Ignore
          }
        }
      }
    } else {
      return new LocalProperties(project, format, new TreeMap<String, FileProperties>());
    }
  }

  public void save() throws CouldNotSavePropertiesException {
    File f = new File(this.project.getLocation().toFile(), CONFIG_FILE_NAME);
    BufferedWriter w = null;
    try {
      w = new BufferedWriter(new FileWriter(f));

      w.write("# HotRot local configuration file for the Eclipse Plugin.\n");
      w.write("# This file usually has per-developer values. " + "It's typically not stored in the SCM repository.\n");

      w.write("\n");
      w.write("format=" + CURRENT_FORMAT + "\n");

      int i = 1;

      DecimalFormat df = new DecimalFormat("0000");

      for (String key : this.files.keySet()) {
        FileProperties fp = this.files.get(key);
        String file = "file" + df.format(i);
        w.write("\n");
        w.write("# Properties for file: " + fp.fileName + "\n");
        w.write(file + "." + FILENAME_ATT + "=" + fp.fileName + "\n");
        w.write(file + "." + DRIVERCLASSPATH_ATT + "=" + ClassPathEncoder.encode(fp.driverClassPathEntries) + "\n");
        w.write(file + "." + DRIVERCLASSNAME_ATT + "=" + fp.driverClassName + "\n");
        w.write(file + "." + URL_ATT + "=" + fp.url + "\n");
        w.write(file + "." + USERNAME_ATT + "=" + fp.username + "\n");
        w.write(file + "." + PASSWORD_ATT + "=" + fp.password + "\n");
        w.write(file + "." + CATALOG_ATT + "=" + fp.catalog + "\n");
        w.write(file + "." + SCHEMA_ATT + "=" + fp.schema + "\n");
        w.write(file + "." + GENERATOR_ATT + "=" + fp.generator + "\n");

        TreeMap<Integer, String> slicedCache = fp.getSlicedCache();
        if (slicedCache != null) {
          int sliceNumber = 0;
          for (String value : slicedCache.values()) {
            String attribute = CACHE_PREFIX_ATT + df.format(sliceNumber);
            w.write(file + "." + attribute + "=" + value + "\n");
            sliceNumber++;
          }
        }

        i++;
      }

    } catch (IOException e) {
      throw new CouldNotSavePropertiesException(e.getMessage());
    } catch (CouldNotEncodeException e) {
      throw new CouldNotSavePropertiesException(e.getMessage());
    } finally {
      if (w != null) {
        try {
          w.close();
        } catch (IOException e) {
          throw new CouldNotSavePropertiesException("Failed while closing file: " + e.getMessage());
        }
      }
    }
  }

  public static class FileProperties {

    private static final int MAX_VALUE_LENGTH = 80;

    private String fileName = null;
    private List<String> driverClassPathEntries = null;
    private String driverClassName = null;
    private String url = null;
    private String username = null;
    private String password = null;
    private String catalog = null;
    private String schema = null;
    private String generator = null;

    private TreeMap<Integer, String> cache = new TreeMap<Integer, String>();
    private MainConfigFile cachedConfigFile = null;

    private List<String> availableCatalogs;
    private List<String> availableSchemas;

    public FileProperties(final String fileName) {
      super();
      this.fileName = fileName;
      this.driverClassPathEntries = new ArrayList<String>();
      this.driverClassName = "";
      this.url = "";
      this.username = "";
      this.password = "";
      this.catalog = "";
      this.schema = "";
      this.generator = "";
    }

    public void set(final String name, final String attribute, final String value)
        throws CouldNotLoadPropertiesException {
      if (FILENAME_ATT.equals(attribute)) {
        this.fileName = value;
      } else if (DRIVERCLASSPATH_ATT.equals(attribute)) {
        this.driverClassPathEntries = ClassPathEncoder.decode(value);
      } else if (DRIVERCLASSNAME_ATT.equals(attribute)) {
        this.driverClassName = value;
      } else if (URL_ATT.equals(attribute)) {
        this.url = value;
      } else if (USERNAME_ATT.equals(attribute)) {
        this.username = value;
      } else if (PASSWORD_ATT.equals(attribute)) {
        this.password = value;
      } else if (CATALOG_ATT.equals(attribute)) {
        this.catalog = value;
      } else if (SCHEMA_ATT.equals(attribute)) {
        this.schema = value;
      } else if (GENERATOR_ATT.equals(attribute)) {
        this.generator = value;
      } else if (attribute.startsWith(CACHE_PREFIX_ATT)) {
        try {
          Integer index = new Integer(attribute.substring(CACHE_PREFIX_ATT.length()));
          this.cache.put(index, value);
        } catch (NumberFormatException e) {
          throw new CouldNotLoadPropertiesException("Unrecognized property name '" + name + "'.");
        }
      } else {
        throw new CouldNotLoadPropertiesException("Unrecognized property name '" + name + "'.");
      }
    }

    public void validate(final String file) throws CouldNotLoadPropertiesException {
      if (this.fileName == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + FILENAME_ATT + "' not found.");
      }
      if (SUtil.isEmpty(this.fileName)) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + FILENAME_ATT + "' is empty.");
      }
      if (this.driverClassPathEntries == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + DRIVERCLASSPATH_ATT + "' not found.");
      }
      if (this.driverClassName == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + DRIVERCLASSNAME_ATT + "' not found.");
      }
      if (this.url == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + URL_ATT + "' not found.");
      }
      if (this.username == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + USERNAME_ATT + "' not found.");
      }
      if (this.password == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + PASSWORD_ATT + "' not found.");
      }
      if (this.catalog == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + CATALOG_ATT + "' not found.");
      }
      if (this.schema == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + SCHEMA_ATT + "' not found.");
      }
      if (this.generator == null) {
        throw new CouldNotLoadPropertiesException(
            "Incomplete file '" + file + "'. Property '" + file + "." + GENERATOR_ATT + "' not found.");
      }

      // Reassemble the cached config file

      try {
        this.cachedConfigFile = ObjectPropertyCodec.decode(this.cache, MainConfigFile.class);
      } catch (CouldNotDecodeException e) {
        throw new CouldNotLoadPropertiesException("Invalid file cache on file '" + file + "': " + e.getMessage());
      }

    }

    public TreeMap<Integer, String> getSlicedCache() throws CouldNotEncodeException {
      if (this.cachedConfigFile == null) {
        return null;
      } else {
        return ObjectPropertyCodec.encode(this.cachedConfigFile, MAX_VALUE_LENGTH);
      }
    }

    // Setters & Getters

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }

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

    public List<String> getAvailableCatalogs() {
      return availableCatalogs;
    }

    public void setAvailableCatalogs(List<String> availableCatalogs) {
      this.availableCatalogs = availableCatalogs;
    }

    public List<String> getAvailableSchemas() {
      return availableSchemas;
    }

    public void setAvailableSchemas(List<String> availableSchemas) {
      this.availableSchemas = availableSchemas;
    }

    public MainConfigFile getCachedConfigFile() {
      return cachedConfigFile;
    }

    public void setCachedConfigFile(MainConfigFile cachedConfigFile) {
      this.cachedConfigFile = cachedConfigFile;
    }

  }

  // Exceptions

  public static class CouldNotLoadPropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotLoadPropertiesException(final String message) {
      super(message);
    }

  }

  public static class CouldNotSavePropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotSavePropertiesException(final String message) {
      super(message);
    }

  }

  public static void log(final String txt) {
    // System.out.println(txt);
  }

}
