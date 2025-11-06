package org.hotrod.livesql.queries.typesolver;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.utils.SUtil;

public class TypeRule {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TypeRule.class.getName());

  private String test;

  private TypeHandler<?, ?> typeHandler; // If test succeeds and there's no error message, this is the outcome
  private String errorMessage; // If test succeeds and there's an error message, a RuntimeException is thrown
                               // with this message

  private DynamicExpressionFactory factory;
  private DynamicExpression testExpression;

  private TypeRule(final String test, final TypeHandler<?, ?> typeHandler, final String errorMessage) {
    this.test = test;
    this.typeHandler = typeHandler;
    this.errorMessage = errorMessage;

    this.factory = DynamicExpressionFactoryConfig.getFactory();
    if (SUtil.isEmpty(this.test)) {
      throw new LiveSQLException("Invalid <type-solver> expression. Must be a non-empty JEXL expression");
    }
    this.testExpression = this.factory.expression(this.test);
  }

  public static TypeRule of(final String test, final TypeHandler<?, ?> typeHandler) {
    return new TypeRule(test, typeHandler, null);
  }

  public static TypeRule of(final String test, final String errorMessage) {
    return new TypeRule(test, null, errorMessage);
  }

  public boolean applies(final ResultSetColumnMetadata cm) throws CouldNotResolveResultSetDataTypeException {
    Parameters context = this.factory.newObjectContext(cm);
    Object v = null;
    try {
      v = this.testExpression.evaluate(context);
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
        return true;
      }
      return false;

    } catch (ClassCastException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "Could not evaluate Type Solver's  <when> tag's test expression '" + this.test
              + "': must return a boolean value but returned a value of type "
              + (v == null ? "null" : v.getClass().getName()));
    } catch (DynamicExpressionException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "Could not evaluate Type Solver's <when> tag's test expression '" + this.test + "': " + e.getMessage()
              + ". The ResultSetMetaData properties that can be used in this expression are:\n" + cm.toString());
    }
  }

  public TypeHandler<?, ?> getTypeHandler() {
    return typeHandler;
  }

  public static class CouldNotResolveResultSetDataTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    private transient ResultSetColumnMetadata cm;

    public CouldNotResolveResultSetDataTypeException(ResultSetColumnMetadata cm, String message) {
      super(message);
      this.cm = cm;
    }

    public ResultSetColumnMetadata getColumnMetaData() {
      return cm;
    }

  }

}
