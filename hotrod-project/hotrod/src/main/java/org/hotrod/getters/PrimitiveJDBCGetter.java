package org.hotrod.getters;

public class PrimitiveJDBCGetter {

  private String resultSetMethod;
  private boolean returnsPrimitiveType;
  private ResultSetGetter rsg;

  private PrimitiveJDBCGetter(String resultSetMethod, boolean returnsPrimitiveType, ResultSetGetter rsg) {
    this.resultSetMethod = resultSetMethod;
    this.returnsPrimitiveType = returnsPrimitiveType;
    this.rsg = rsg;
  }

  public static PrimitiveJDBCGetter obj(String m, ResultSetGetter rsg) {
    return new PrimitiveJDBCGetter(m, false, rsg);
  }

  public static PrimitiveJDBCGetter prim(String m, ResultSetGetter rsg) {
    return new PrimitiveJDBCGetter(m, true, rsg);
  }

  public String getResultSetMethod() {
    return resultSetMethod;
  }

  public boolean returnsPrimitiveType() {
    return returnsPrimitiveType;
  }

  public final ResultSetGetter getResultSetGetter() {
    return rsg;
  }

}
