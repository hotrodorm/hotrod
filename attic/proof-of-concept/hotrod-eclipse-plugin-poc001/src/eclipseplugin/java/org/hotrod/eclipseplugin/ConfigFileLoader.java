package org.hotrod.eclipseplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.hotrod.domain.ConfigItem;
import org.hotrod.domain.Converter;
import org.hotrod.domain.DAO;
import org.hotrod.domain.EnumDAO;
import org.hotrod.domain.ExecutorDAO;
import org.hotrod.domain.FragmentConfigFile;
import org.hotrod.domain.MainConfigFile;
import org.hotrod.domain.Method;
import org.hotrod.domain.QueryMethod;
import org.hotrod.domain.SelectMethod;
import org.hotrod.domain.SequenceMethod;
import org.hotrod.domain.Settings;
import org.hotrod.domain.TableDAO;
import org.hotrod.domain.ViewDAO;
import org.hotrod.domain.loader.FaultyConfigFileException;
import org.hotrod.domain.loader.NameContent;
import org.hotrod.domain.loader.UnreadableConfigFileException;
import org.hotrod.eclipseplugin.FaceProducer.RelativeProjectPath;

public class ConfigFileLoader {

  // Load main file

  public static MainConfigFile loadMainFile(final File f, final RelativeProjectPath path)
      throws FaultyConfigFileException {

    BufferedReader r = null;

    try {

      r = new BufferedReader(new FileReader(f));

      MainConfigFile config = new MainConfigFile(f, path);
      ConfigItem currentItem = null;

      boolean headerWasRead = false;

      String line = null;
      int lineNumber = 1;
      while ((line = r.readLine()) != null) {
        if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
          if (!headerWasRead) {
            if ("HOTROD-CONFIG".equals(line.trim())) {
              headerWasRead = true;
            } else {
              throw new FaultyConfigFileException(path, lineNumber,
                  "This is not a main HotRod config file: missing header.");
            }
          } else {
            ConfigItem item = tryReadingItem(path, line, lineNumber);
            if (item != null) {
              if (item instanceof FragmentConfigFile) {
                FragmentConfigFile fc = (FragmentConfigFile) item;
                log("--> will load fragment: " + fc.getIncluderRelativePath());
                try {
                  fc = loadFragmentFile(f, path, fc.getIncluderRelativePath());
                } catch (UnreadableConfigFileException e) {
                  throw new FaultyConfigFileException(path, lineNumber, e.getMessage());
                }
                config.addConfigItem(fc);
                currentItem = fc;
              } else {
                config.addConfigItem(item);
                currentItem = item;
              }
            } else {
              Method m = tryReadingMethod(path, line, lineNumber);
              if (m != null) {
                if (currentItem == null) {
                  throw new FaultyConfigFileException(path, lineNumber, "There's no DAO to include this method in.");
                } else {
                  try {
                    DAO d = (DAO) currentItem;
                    d.addMethod(m);
                  } catch (ClassCastException e) {
                    throw new FaultyConfigFileException(path, lineNumber, "The previous item cannot include methods.");
                  }
                }
              } else {
                log("FaultyConfigFileException: path=" + path);
                throw new FaultyConfigFileException(path, lineNumber, "Invalid line: " + line);
              }
            }
          }
        }
        lineNumber++;
      }

      return config;

    } catch (FileNotFoundException e) {
      throw new FaultyConfigFileException(path, 1, "File " + f.getPath() + " not found.");
    } catch (IOException e) {
      throw new FaultyConfigFileException(path, 1, "Could not read file " + f.getPath() + ":\n" + e.getMessage());
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

  // Load fragment

  public static FragmentConfigFile loadFragmentFile(final File includerFile, final RelativeProjectPath includersPath,
      final String relativePathName) throws FaultyConfigFileException, UnreadableConfigFileException {

    // Resolve file system location of the configuration file

    // File f;
    //
    // if (includerFile == null) {
    // IWorkspace workspace = ResourcesPlugin.getWorkspace();
    // IProject myProject = workspace.getRoot().getProject("project002");
    // if (myProject.exists() && !myProject.isOpen()) {
    // try {
    // myProject.open(null);
    // } catch (CoreException e) {
    // throw new UnreadableConfigFileException("Could not open project: " +
    // e.getMessage());
    // }
    // }
    // // log("fileName=" + fileName);
    // IFile fileResource = myProject.getFile(relativePathName);
    // // log("fileResource=" + fileResource);
    // IPath path = fileResource.getLocation();
    // // log("path=" + path);
    // f = path.toFile();
    // } else {
    // f = new File(includerFile.getParentFile(), relativePathName);
    // }

    File includerFolder = includerFile.getParentFile();
    File f = new File(includerFolder, relativePathName);

    RelativeProjectPath fragmentPath = RelativeProjectPath.findRelativePath(includersPath.getProject(), f);
    log("======> relPath=" + fragmentPath.getRelativePath());

    // Read the configuration file

    BufferedReader r = null;

    try {

      r = new BufferedReader(new FileReader(f));

      FragmentConfigFile config = new FragmentConfigFile(f, fragmentPath, relativePathName, 1);
      ConfigItem currentItem = null;

      boolean headerWasRead = false;
      String line = null;
      int lineNumber = 1;
      while ((line = r.readLine()) != null) {
        if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
          if (!headerWasRead) {
            if ("HOTROD-FRAGMENT-CONFIG".equals(line)) {
              headerWasRead = true;
            } else {
              throw new FaultyConfigFileException(fragmentPath, lineNumber,
                  "This is not a fragment HotRod config file: missing header.");
            }
          } else {
            ConfigItem item = tryReadingItem(fragmentPath, line, lineNumber);
            if (item != null) {
              if (item instanceof FragmentConfigFile) {
                FragmentConfigFile fc = (FragmentConfigFile) item;
                log("--> will load fragment: " + fc.getIncluderRelativePath());
                try {
                  fc = loadFragmentFile(f, fragmentPath, fc.getIncluderRelativePath());
                  config.addConfigItem(fc);
                  currentItem = fc;
                } catch (UnreadableConfigFileException e) {
                  throw new FaultyConfigFileException(fragmentPath, lineNumber, e.getMessage());
                }
              } else {
                config.addConfigItem(item);
                currentItem = item;
              }
            } else {
              Method m = tryReadingMethod(fragmentPath, line, lineNumber);
              if (m != null) {
                if (currentItem == null) {
                  throw new FaultyConfigFileException(fragmentPath, lineNumber,
                      "There's no DAO to include this method in.");
                } else {
                  try {
                    DAO d = (DAO) currentItem;
                    d.addMethod(m);
                  } catch (ClassCastException e) {
                    throw new FaultyConfigFileException(fragmentPath, lineNumber,
                        "The previous item cannot include methods.");
                  }
                }
              } else {
                // System.out.println("[as] fragmentPath=" + fragmentPath);
                throw new FaultyConfigFileException(fragmentPath, lineNumber, "Invalid line: " + line);
              }
            }
          }
        }
        lineNumber++;
      }

      return config;

    } catch (FileNotFoundException e) {
      throw new UnreadableConfigFileException("File " + relativePathName + " not found.");
    } catch (IOException e) {
      throw new UnreadableConfigFileException("Could not read file " + relativePathName + ":\n" + e.getMessage());
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

  // Simple parser

  private static final String SETTINGS_PROMPT = "settings:";
  private static final String TABLE_DAO_PROMPT = "table:";
  private static final String VIEW_DAO_PROMPT = "view:";
  private static final String ENUM_DAO_PROMPT = "enum:";
  private static final String EXECUTOR_DAO_PROMPT = "executor:";
  private static final String CONVERTER_PROMPT = "converter:";
  private static final String FRAGMENT_PROMPT = "fragment:";

  private static ConfigItem tryReadingItem(final RelativeProjectPath path, final String rawLine, final int lineNumber)
      throws FaultyConfigFileException {
    String line = rawLine.trim();
    if (line.startsWith(TABLE_DAO_PROMPT)) {
      NameContent nc = new NameContent(line, TABLE_DAO_PROMPT);
      log("nc.getName()=" + nc.getName() + " nc.getName().isEmpty()=" + nc.getName().isEmpty());
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The table must have a name.");
      }
      return new TableDAO(nc, lineNumber);
    } else if (line.startsWith(VIEW_DAO_PROMPT)) {
      NameContent nc = new NameContent(line, VIEW_DAO_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The view must have a name.");
      }
      return new ViewDAO(nc, lineNumber);
    } else if (line.startsWith(ENUM_DAO_PROMPT)) {
      NameContent nc = new NameContent(line, ENUM_DAO_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The enum must have a name.");
      }
      return new EnumDAO(nc, lineNumber);
    } else if (line.startsWith(EXECUTOR_DAO_PROMPT)) {
      NameContent nc = new NameContent(line, EXECUTOR_DAO_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The executor must have a name.");
      }
      return new ExecutorDAO(nc, lineNumber);
    } else if (line.startsWith(CONVERTER_PROMPT)) {
      NameContent nc = new NameContent(line, CONVERTER_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The converter must have a name.");
      }
      return new Converter(nc, lineNumber);
    } else if (line.startsWith(SETTINGS_PROMPT)) {
      NameContent nc = new NameContent(line, SETTINGS_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The settings must have a name.");
      }
      return new Settings(nc, lineNumber);
    } else if (line.startsWith(FRAGMENT_PROMPT)) {
      String relativeFileName = line.substring(FRAGMENT_PROMPT.length()).trim();
      if (relativeFileName.isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The fragment dao must have a name.");
      }
      File fragment = new File(relativeFileName);
      log("[FRAGMENT] relativeFileName=" + relativeFileName + " file=" + fragment.getPath());
      // FIXME
      return new FragmentConfigFile(fragment, null, relativeFileName, lineNumber);
    } else {
      return null;
    }
  }

  private static final String SEQUENCE_METHOD_PROMPT = "sequence:";
  private static final String QUERY_METHOD_PROMPT = "query:";
  private static final String SELECT_METHOD_PROMPT = "select:";

  private static Method tryReadingMethod(final RelativeProjectPath path, final String rawLine, final int lineNumber)
      throws FaultyConfigFileException {
    String line = rawLine.trim();
    if (line.startsWith(SEQUENCE_METHOD_PROMPT)) {
      NameContent nc = new NameContent(line, SEQUENCE_METHOD_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The sequence must have a name.");
      }
      return new SequenceMethod(nc, lineNumber);
    } else if (line.startsWith(QUERY_METHOD_PROMPT)) {
      NameContent nc = new NameContent(line, QUERY_METHOD_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The query must have a name.");
      }
      return new QueryMethod(nc, lineNumber);
    } else if (line.startsWith(SELECT_METHOD_PROMPT)) {
      NameContent nc = new NameContent(line, SELECT_METHOD_PROMPT);
      if (nc.getName().isEmpty()) {
        throw new FaultyConfigFileException(path, lineNumber, "The select dao must have a name.");
      }
      return new SelectMethod(nc, lineNumber);
    } else {
      return null;
    }
  }

  public static void log(final String txt) {
    System.out.println("[" + ConfigFileLoader.class.getName() + "] " + txt);
  }

}
