package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;

public interface SQLSegment {

  public abstract boolean isEmpty();

  public abstract String renderStatic(ParameterRenderer parameterRenderer);

  public abstract String renderSQLFoundation(ParameterRenderer parameterRenderer);

}
