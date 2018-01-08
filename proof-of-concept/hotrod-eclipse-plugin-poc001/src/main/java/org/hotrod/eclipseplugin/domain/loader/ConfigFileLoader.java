package org.hotrod.eclipseplugin.domain.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.Converter;
import org.hotrod.eclipseplugin.domain.DAO;
import org.hotrod.eclipseplugin.domain.EnumDAO;
import org.hotrod.eclipseplugin.domain.ExecutorDAO;
import org.hotrod.eclipseplugin.domain.FragmentConfigFile;
import org.hotrod.eclipseplugin.domain.MainConfigFile;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.QueryMethod;
import org.hotrod.eclipseplugin.domain.SelectMethod;
import org.hotrod.eclipseplugin.domain.SequenceMethod;
import org.hotrod.eclipseplugin.domain.Settings;
import org.hotrod.eclipseplugin.domain.TableDAO;
import org.hotrod.eclipseplugin.domain.ViewDAO;

public class ConfigFileLoader {

  public static <T extends MainConfigFile> T loadConfigFile(final String fileName, final Class<T> c)
      throws UnreadableConfigFileException, FaultyConfigFileException {

    BufferedReader r = null;

    // org.eclipse.core.resources.IResource a;

    IPath path = org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot().getLocation();
    // IPath path =
    // org.eclipse.core.resources.ResourcesPlugin.getWorkspace().getRoot()
    IPath filePath = path.append(fileName);
    File f = filePath.toFile();
    System.out.println("READING f=" + f.getPath() + " / " + f.getAbsolutePath());

    // System.out.println("ipath=" + path);

    try {

      r = new BufferedReader(new FileReader(f));

      FragmentConfigFile config = new FragmentConfigFile(fileName);
      ConfigItem currentItem = null;

      String line = null;
      int lineNumber = 1;
      while ((line = r.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          ConfigItem item = tryReadingItem(line, lineNumber);
          if (item != null) {
            if (item instanceof FragmentConfigFile) {
              FragmentConfigFile fc = (FragmentConfigFile) item;
              System.out.println("--> will load fragment: " + fc.getFileName());
              fc = loadConfigFile(fc.getFileName(), FragmentConfigFile.class);
              config.addConfigItem(fc);
              currentItem = fc;
            } else {
              config.addConfigItem(item);
              currentItem = item;
            }
          } else {
            Method m = tryReadingMethod(line, lineNumber);
            if (m != null) {
              if (currentItem == null) {
                throw new FaultyConfigFileException(lineNumber, "There's no DAO to include this method in.");
              } else {
                try {
                  DAO d = (DAO) currentItem;
                  d.addMethod(m);
                } catch (ClassCastException e) {
                  throw new FaultyConfigFileException(lineNumber, "The previous item cannot include methods.");
                }
              }
            } else {
              throw new FaultyConfigFileException(lineNumber, "Invalid line: " + line);
            }
          }
        }
        lineNumber++;
      }

      return c.cast(config);

    } catch (FileNotFoundException e) {
      throw new UnreadableConfigFileException("File " + f.getPath() + " does not exist.");
    } catch (IOException e) {
      throw new UnreadableConfigFileException("Could not read file " + f.getPath() + ": " + e.getMessage());
    } finally {
      if (r != null) {
        try {
          r.close();
        } catch (IOException e) {
          // Ignore
        }
      }
    }

  }

  private static final String SETTINGS_PROMPT = "settings:";
  private static final String TABLE_DAO_PROMPT = "table:";
  private static final String VIEW_DAO_PROMPT = "view:";
  private static final String ENUM_DAO_PROMPT = "enum:";
  private static final String EXECUTOR_DAO_PROMPT = "executor:";
  private static final String CONVERTER_PROMPT = "converter:";
  private static final String FRAGMENT_PROMPT = "fragment:";

  private static ConfigItem tryReadingItem(final String rawLine, final int lineNumber)
      throws FaultyConfigFileException {
    String line = rawLine.trim();
    if (line.startsWith(TABLE_DAO_PROMPT)) {
      String name = line.substring(TABLE_DAO_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The table must have a name.");
      }
      return new TableDAO(name);
    } else if (line.startsWith(VIEW_DAO_PROMPT)) {
      String name = line.substring(VIEW_DAO_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The view must have a name.");
      }
      return new ViewDAO(name);
    } else if (line.startsWith(ENUM_DAO_PROMPT)) {
      String name = line.substring(ENUM_DAO_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The enum must have a name.");
      }
      return new EnumDAO(name);
    } else if (line.startsWith(EXECUTOR_DAO_PROMPT)) {
      String name = line.substring(EXECUTOR_DAO_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The executor must have a name.");
      }
      return new ExecutorDAO(name);
    } else if (line.startsWith(CONVERTER_PROMPT)) {
      NameContent nc = new NameContent(line, CONVERTER_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The converter must have a name.");
      }
      return new Converter(nc);
    } else if (line.startsWith(SETTINGS_PROMPT)) {
      NameContent nc = new NameContent(line, SETTINGS_PROMPT);
      return new Settings(nc);
    } else if (line.startsWith(FRAGMENT_PROMPT)) {
      String fileName = line.substring(FRAGMENT_PROMPT.length()).trim();
      if (fileName.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The fragment dao must have a name.");
      }
      return new FragmentConfigFile(fileName);
    } else {
      return null;
    }
  }

  private static final String SEQUENCE_METHOD_PROMPT = "sequence:";
  private static final String QUERY_METHOD_PROMPT = "query:";
  private static final String SELECT_METHOD_PROMPT = "select:";

  private static Method tryReadingMethod(final String rawLine, final int lineNumber) throws FaultyConfigFileException {
    String line = rawLine.trim();
    if (line.startsWith(SEQUENCE_METHOD_PROMPT)) {
      NameContent nc = new NameContent(line, SEQUENCE_METHOD_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The sequence must have a name.");
      }
      return new SequenceMethod(nc);
    } else if (line.startsWith(QUERY_METHOD_PROMPT)) {
      NameContent nc = new NameContent(line, QUERY_METHOD_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The query must have a name.");
      }
      return new QueryMethod(nc);
    } else if (line.startsWith(SELECT_METHOD_PROMPT)) {
      NameContent nc = new NameContent(line, SELECT_METHOD_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The select dao must have a name.");
      }
      return new SelectMethod(nc);
    } else {
      return null;
    }
  }

  public static class NameContent {

    private String name;
    private String content;

    public NameContent(final String line, final String prompt) {
      String tail = line.substring(prompt.length()).trim();
      int colon = tail.indexOf(':');
      if (colon != -1) {
        this.name = tail.substring(0, colon).trim();
        this.content = tail.substring(colon + 1).trim();
      } else {
        this.name = tail;
        this.content = "";
      }
    }

    public String getName() {
      return name;
    }

    public String getContent() {
      return content;
    }

  }

}
