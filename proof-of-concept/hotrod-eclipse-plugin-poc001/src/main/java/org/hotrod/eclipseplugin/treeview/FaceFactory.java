package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.Converter;
import org.hotrod.eclipseplugin.domain.EnumDAO;
import org.hotrod.eclipseplugin.domain.ExecutorDAO;
import org.hotrod.eclipseplugin.domain.FragmentConfigFile;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.QueryMethod;
import org.hotrod.eclipseplugin.domain.SelectMethod;
import org.hotrod.eclipseplugin.domain.SequenceMethod;
import org.hotrod.eclipseplugin.domain.Settings;
import org.hotrod.eclipseplugin.domain.TableDAO;
import org.hotrod.eclipseplugin.domain.ViewDAO;

public class FaceFactory {

  public static AbstractFace getFace(final ConfigItem item) throws InvalidConfigurationItemException {
    if (item == null) {
      throw new InvalidConfigurationItemException(item.getLineNumber(),
          "Cannot produce a configuration item from a null value.");
    }
    if (item instanceof Settings) {
      return new SettingsFace((Settings) item);
    }
    if (item instanceof TableDAO) {
      return new TableFace((TableDAO) item);
    }
    if (item instanceof ViewDAO) {
      return new ViewFace((ViewDAO) item);
    }
    if (item instanceof EnumDAO) {
      return new EnumFace((EnumDAO) item);
    }
    if (item instanceof ExecutorDAO) {
      return new ExecutorFace((ExecutorDAO) item);
    }
    if (item instanceof Converter) {
      return new ConverterFace((Converter) item);
    }
    if (item instanceof FragmentConfigFile) {
      return new FragmentConfigFace((FragmentConfigFile) item);
    }
    throw new InvalidConfigurationItemException(item.getLineNumber(),
        "Unknown configuration item type '" + item.getClass().getName() + "'.");
  }

  public static AbstractFace getMethodElement(final Method m) throws InvalidConfigurationItemException {
    if (m == null) {
      throw new InvalidConfigurationItemException(m.getLineNumber(),
          "Cannot produce a method element from a null value.");
    }
    if (m instanceof SequenceMethod) {
      return new SequenceFace((SequenceMethod) m);
    }
    if (m instanceof QueryMethod) {
      return new QueryFace((QueryMethod) m);
    }
    if (m instanceof SelectMethod) {
      return new SelectFace((SelectMethod) m);
    }
    throw new InvalidConfigurationItemException(m.getLineNumber(),
        "Unknown configuration method type '" + m.getClass().getName() + "'.");
  }

  public static class InvalidConfigurationItemException extends Exception {

    private static final long serialVersionUID = 1L;

    private int lineNumber;

    public InvalidConfigurationItemException(final int lineNumber, final String message) {
      super(message);
      this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
      return lineNumber;
    }

  }

}
