package org.hotrod.dynamicsql.assembler;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class DynamicSQL extends Sequence {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(DynamicSQL.class.getName());

  public DynamicSQL() {
    super();
    super.setMe(this);
  }

  public DynamicSQL(DynamicExpressionFactory factory) {
    super(factory);
    super.setMe(this);
  }

  public Parameters newParameters() {
    return this.factory.newParameterContext();
  }

}
