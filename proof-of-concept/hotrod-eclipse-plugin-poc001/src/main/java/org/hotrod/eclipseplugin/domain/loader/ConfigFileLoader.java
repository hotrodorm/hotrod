package org.hotrod.eclipseplugin.domain.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.hotrod.eclipseplugin.domain.DAO;
import org.hotrod.eclipseplugin.domain.ExecutorDAO;
import org.hotrod.eclipseplugin.domain.FragmentConfigFile;
import org.hotrod.eclipseplugin.domain.MainConfigFile;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.QueryMethod;
import org.hotrod.eclipseplugin.domain.SelectMethod;
import org.hotrod.eclipseplugin.domain.SequenceMethod;
import org.hotrod.eclipseplugin.domain.TableDAO;
import org.hotrod.eclipseplugin.domain.ViewDAO;

public class ConfigFileLoader {

  public static MainConfigFile loadMainConfigFile(final File f)
      throws UnreadableConfigFileException, FaultyConfigFileException {

    BufferedReader r = null;

    try {
      r = new BufferedReader(new FileReader(f));

      MainConfigFile config = new MainConfigFile(f.getPath());
      DAO currentDAO = null;

      String line = null;
      int lineNumber = 1;
      while ((line = r.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          DAO d = tryReadingDAO(line, lineNumber);
          if (d != null) {
            if (d instanceof FragmentConfigFile) {
              FragmentConfigFile fc = (FragmentConfigFile) d;
              fc = loadFragmentConfigFile(new File(fc.getFileName()));
              config.addDAO(fc);
              currentDAO = fc;
            } else {
              config.addDAO(d);
              currentDAO = d;
            }
          } else {
            Method m = tryReadingMethod(line, lineNumber);
            if (m != null) {
              if (currentDAO == null) {
                throw new FaultyConfigFileException(lineNumber, "No DAO to include this method.");
              } else if (d instanceof FragmentConfigFile) {
                throw new FaultyConfigFileException(lineNumber, "DAO cannot be included in a fragment declaration.");
              } else {
                currentDAO.addMethod(m);
              }
            } else {
              throw new FaultyConfigFileException(lineNumber, "Invalid line: " + line);
            }
          }
        }
        lineNumber++;
      }

      return config;

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

  public static FragmentConfigFile loadFragmentConfigFile(final File f)
      throws UnreadableConfigFileException, FaultyConfigFileException {

    BufferedReader r = null;

    try {
      r = new BufferedReader(new FileReader(f));

      FragmentConfigFile config = new FragmentConfigFile(f.getPath());
      DAO currentDAO = null;

      String line = null;
      int lineNumber = 1;
      while ((line = r.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          DAO d = tryReadingDAO(line, lineNumber);
          if (d != null) {
            if (d instanceof FragmentConfigFile) {
              FragmentConfigFile fc = (FragmentConfigFile) d;
              fc = loadFragmentConfigFile(new File(fc.getFileName()));
              config.addDAO(fc);
              currentDAO = fc;
            } else {
              config.addDAO(d);
              currentDAO = d;
            }
          } else {
            Method m = tryReadingMethod(line, lineNumber);
            if (m != null) {
              if (currentDAO == null) {
                throw new FaultyConfigFileException(lineNumber, "No DAO to include this method.");
              } else if (d instanceof FragmentConfigFile) {
                throw new FaultyConfigFileException(lineNumber, "DAO cannot be included in a fragment declaration.");
              } else {
                currentDAO.addMethod(m);
              }
            } else {
              throw new FaultyConfigFileException(lineNumber, "Invalid line: " + line);
            }
          }
        }
        lineNumber++;
      }

      return config;

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

  private static final String TABLE_DAO_PROMPT = "table:";
  private static final String VIEW_DAO_PROMPT = "view:";
  private static final String EXECUTOR_DAO_PROMPT = "dao:";
  private static final String FRAGMENT_DAO_PROMPT = "fragment:";

  private static DAO tryReadingDAO(final String line, final int lineNumber) throws FaultyConfigFileException {
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
    } else if (line.startsWith(EXECUTOR_DAO_PROMPT)) {
      String name = line.substring(EXECUTOR_DAO_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The executor dao must have a name.");
      }
      return new ExecutorDAO(name);
    } else if (line.startsWith(FRAGMENT_DAO_PROMPT)) {
      String fileName = line.substring(FRAGMENT_DAO_PROMPT.length()).trim();
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

  private static Method tryReadingMethod(final String line, final int lineNumber) throws FaultyConfigFileException {
    if (line.startsWith(SEQUENCE_METHOD_PROMPT)) {
      String name = line.substring(SEQUENCE_METHOD_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The sequence must have a name.");
      }
      return new SequenceMethod(name);
    } else if (line.startsWith(QUERY_METHOD_PROMPT)) {
      String name = line.substring(QUERY_METHOD_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The query must have a name.");
      }
      return new QueryMethod(name);
    } else if (line.startsWith(SELECT_METHOD_PROMPT)) {
      String name = line.substring(SELECT_METHOD_PROMPT.length()).trim();
      if (name.isEmpty()) {
        throw new FaultyConfigFileException(lineNumber, "The select dao must have a name.");
      }
      return new SelectMethod(name);
    } else {
      return null;
    }
  }

}
