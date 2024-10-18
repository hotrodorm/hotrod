package org.hotrod.runtime.livesql.queries.typesolver;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.typesolver.OGNLPublicMemberAccess;
import org.hotrodorm.hotrod.utils.SUtil;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class TypeRule {

  private String test;

  private TypeHandler typeHandler; // If test succeeds and there's no error message, this is the outcome
  private String errorMessage; // If test succeeds and there's an error message, a RuntimeException is thrown
                               // with this message

  private OgnlContext context = new OgnlContext(null, null, new OGNLPublicMemberAccess());
  private Object testExpression = null;

  private TypeRule(final String test, final TypeHandler typeHandler, final String errorMessage) {
    this.test = test;
    this.typeHandler = typeHandler;
    this.errorMessage = errorMessage;

    try {
      if (SUtil.isEmpty(this.test)) {
        throw new LiveSQLException("Invalid <type-solver> expression. Must be a non-empty OGNL expression");
      }
      this.testExpression = Ognl.parseExpression(this.test);
    } catch (OgnlException e) {
      throw new LiveSQLException(
          "Invalid <type-solver> expression '" + this.test + "'. Must be a valid OGNL expression", e);
    }
  }

  public static TypeRule of(final String test, final TypeHandler typeHandler) {
    return new TypeRule(test, typeHandler, null);
  }

  public static TypeRule of(final String test, final String errorMessage) {
    return new TypeRule(test, null, errorMessage);
  }

  public TypeHandler resolve(final ResultSetColumnMetadata cm) throws CouldNotResolveResultSetDataTypeException {
    Object v = null;
    try {
      v = Ognl.getValue(this.testExpression, context, cm);
      if (v == null) {
        throw new CouldNotResolveResultSetDataTypeException(cm,
            "Could not evaluate Type Solver's <when> tag's test expression '" + this.test
                + "': must return a boolean value but returned null");
      }
      Boolean ruleApplies = (Boolean) v;
      if (ruleApplies) {
        if (this.errorMessage != null) {
          throw new CouldNotResolveResultSetDataTypeException(cm, "Type Solver's with test expression '" + this.test
              + "' returned the error message:\n" + this.errorMessage);
        }
        return this.typeHandler;
      }
      return null;

    } catch (ClassCastException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "Could not evaluate Type Solver's  <when> tag's test expression '" + this.test
              + "': must return a boolean value but returned a value of type " + v.getClass().getName());
    } catch (OgnlException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "Could not evaluate Type Solver's <when> tag's test expression '" + this.test + "': " + e.getMessage());
    }
  }

  public static class CouldNotResolveResultSetDataTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    private ResultSetColumnMetadata cm;

    public CouldNotResolveResultSetDataTypeException(ResultSetColumnMetadata cm, String message) {
      super(message);
      this.cm = cm;
    }

    public ResultSetColumnMetadata getColumnMetaData() {
      return cm;
    }

  }

}
