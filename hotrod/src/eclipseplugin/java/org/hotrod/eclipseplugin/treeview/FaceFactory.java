package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.EnumTag;
import org.hotrod.config.FragmentTag;
import org.hotrod.config.GeneratorsTag;
import org.hotrod.config.ExecutorTag;
import org.hotrod.config.QueryMethodTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.SequenceMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.runtime.dynamicsql.SourceLocation;

public class FaceFactory {

  public static AbstractFace getFace(final AbstractConfigurationTag tag) throws InvalidConfigurationItemException {
    if (tag == null) {
      throw new InvalidConfigurationItemException(null, "Cannot produce a configuration item from a null value.");
    }
    if (tag instanceof GeneratorsTag) {
      return new SettingsFace((GeneratorsTag) tag);
    }
    if (tag instanceof TableTag) {
      return new TableFace((TableTag) tag);
    }
    if (tag instanceof ViewTag) {
      return new ViewFace((ViewTag) tag);
    }
    if (tag instanceof EnumTag) {
      return new EnumFace((EnumTag) tag);
    }
    if (tag instanceof ExecutorTag) {
      return new ExecutorFace((ExecutorTag) tag);
    }
    if (tag instanceof ConverterTag) {
      return new ConverterFace((ConverterTag) tag);
    }
    if (tag instanceof FragmentTag) {
      return new FragmentConfigFace((FragmentTag) tag);
    }
    throw new InvalidConfigurationItemException(tag.getSourceLocation(),
        "Unknown configuration item type '" + tag.getClass().getName() + "'.");
  }

  public static AbstractFace getMethodElement(final AbstractMethodTag<?> m) throws InvalidConfigurationItemException {
    if (m instanceof SequenceMethodTag) {
      return new SequenceFace((SequenceMethodTag) m);
    }
    if (m instanceof QueryMethodTag) {
      return new QueryFace((QueryMethodTag) m);
    }
    if (m instanceof SelectMethodTag) {
      return new SelectFace((SelectMethodTag) m);
    }
    throw new InvalidConfigurationItemException(m.getSourceLocation(),
        "Unknown configuration method type '" + m.getClass().getName() + "'.");
  }

  public static class InvalidConfigurationItemException extends Exception {

    private static final long serialVersionUID = 1L;

    private SourceLocation location;

    public InvalidConfigurationItemException(final SourceLocation location, final String message) {
      super(message);
      this.location = location;
    }

    public SourceLocation getLocation() {
      return location;
    }

  }

}
