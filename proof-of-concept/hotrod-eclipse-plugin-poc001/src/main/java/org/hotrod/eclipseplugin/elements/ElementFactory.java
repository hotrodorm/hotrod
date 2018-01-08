package org.hotrod.eclipseplugin.elements;

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

public class ElementFactory {

  public static TreeElement getElement(final ConfigItem item) throws InvalidConfigurationItemException {
    if (item == null) {
      throw new InvalidConfigurationItemException("Cannot produce a configuration item from a null value.");
    }
    if (item instanceof Settings) {
      return new SettingsElement((Settings) item);
    }
    if (item instanceof TableDAO) {
      return new TableElement((TableDAO) item);
    }
    if (item instanceof ViewDAO) {
      return new ViewElement((ViewDAO) item);
    }
    if (item instanceof EnumDAO) {
      return new EnumElement((EnumDAO) item);
    }
    if (item instanceof ExecutorDAO) {
      return new ExecutorElement((ExecutorDAO) item);
    }
    if (item instanceof Converter) {
      return new ConverterElement((Converter) item);
    }
    if (item instanceof FragmentConfigFile) {
      return new FragmentConfigElement((FragmentConfigFile) item);
    }
    throw new InvalidConfigurationItemException("Unknown configuration item type '" + item.getClass().getName() + "'.");
  }

  public static TreeLeafElement getMethodElement(final Method m) throws InvalidConfigurationItemException {
    if (m == null) {
      throw new InvalidConfigurationItemException("Cannot produce a method element from a null value.");
    }
    if (m instanceof SequenceMethod) {
      return new SequenceElement((SequenceMethod) m);
    }
    if (m instanceof QueryMethod) {
      return new QueryElement((QueryMethod) m);
    }
    if (m instanceof SelectMethod) {
      return new SelectElement((SelectMethod) m);
    }
    throw new InvalidConfigurationItemException("Unknown configuration method type '" + m.getClass().getName() + "'.");
  }

  public static class InvalidConfigurationItemException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidConfigurationItemException(final String message) {
      super(message);
    }

  }

}
