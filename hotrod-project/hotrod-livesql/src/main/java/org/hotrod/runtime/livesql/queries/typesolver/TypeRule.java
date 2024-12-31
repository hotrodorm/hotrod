package org.hotrod.runtime.livesql.queries.typesolver;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.utils.SUtil;

public class TypeRule {

  private static final Logger log = Logger.getLogger(TypeRule.class.getName());

  private int ruleNumber;

  private String test;

  private TypeHandler typeHandler; // If test succeeds and there's no error message, this is the outcome
  private String errorMessage; // If test succeeds and there's an error message, a RuntimeException is thrown
                               // with this message

  private DynamicExpressionFactory factory;
  private DynamicExpression testExpression;

  private TypeRule(final int ruleNumber, final String test, final TypeHandler typeHandler, final String errorMessage) {
    this.ruleNumber = ruleNumber;
    this.test = test;
    this.typeHandler = typeHandler;
    this.errorMessage = errorMessage;

    this.factory = DynamicExpressionFactoryConfig.getFactory();
    if (SUtil.isEmpty(this.test)) {
      throw new LiveSQLException("Invalid <type-solver> expression. Must be a non-empty OGNL expression");
    }
    this.testExpression = this.factory.expression(this.test);
  }

  public static TypeRule of(final String test, final TypeHandler typeHandler, final int ruleNumber) {
    return new TypeRule(ruleNumber, test, typeHandler, null);
  }

  public static TypeRule of(final String test, final String errorMessage, final int ruleNumber) {
    return new TypeRule(ruleNumber, test, null, errorMessage);
  }

  public TypeHandler resolve(final ResultSetColumnMetadata cm) throws CouldNotResolveResultSetDataTypeException {
    ParameterContext context = this.factory.newObjectContext(cm);
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
        return this.typeHandler;
      }
      return null;

    } catch (ClassCastException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "Could not evaluate Type Solver's  <when> tag's test expression '" + this.test
              + "': must return a boolean value but returned a value of type "
              + (v == null ? "null" : v.getClass().getName()));
    } catch (DynamicExpressionException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "Could not evaluate Type Solver's <when> tag's test expression '" + this.test + "': " + e.getMessage());
    }
  }

  public TypeHandler getTypeHandler() {
    return typeHandler;
  }

  public int getRuleNumber() {
    return ruleNumber;
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
