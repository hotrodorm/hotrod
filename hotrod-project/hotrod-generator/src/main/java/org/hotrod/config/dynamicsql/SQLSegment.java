package org.hotrod.config.dynamicsql;

import java.io.Serializable;

import org.hotrod.generator.ParameterRenderer;

public interface SQLSegment extends Serializable {

  public abstract boolean isEmpty();

  public abstract String renderStatic(ParameterRenderer parameterRenderer);

  public abstract String renderSQLFoundation(ParameterRenderer parameterRenderer);

  public abstract String renderXML(ParameterRenderer parameterRenderer);

}
