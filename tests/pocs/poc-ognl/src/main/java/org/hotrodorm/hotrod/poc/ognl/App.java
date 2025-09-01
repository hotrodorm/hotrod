package org.hotrodorm.hotrod.poc.ognl;

import java.util.HashMap;
import java.util.Map;

import ognl.OgnlContext;
import ognl.OgnlException;

public class App {

  public static void main(final String[] args) throws OgnlException {

    System.out.println(">>> OGNL TEST");

    // Create an OGNL context

    OgnlContext context = new OgnlContext(null, null, new PublicMemberAccess());

    // Create a scope (aka "root object")

    Map<String, Object> scope = new HashMap<>();
    scope.put("a", 123);
    scope.put("b", 456);
    scope.put("c", 789);

    // Create an expression

    OgnlExpression expr = new OgnlExpression("a + b");

    // Evaluate the expression

    Object result = expr.getValue(context, scope);

    System.out.println(">>> result=" + result);
  }

}
