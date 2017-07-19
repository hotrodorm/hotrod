package dynamicsql.core;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.expressions.BindExpression;
import org.hotrod.runtime.dynamicsql.expressions.ChooseExpression;
import org.hotrod.runtime.dynamicsql.expressions.CollectionExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.IfExpression;
import org.hotrod.runtime.dynamicsql.expressions.OtherwiseExpression;
import org.hotrod.runtime.dynamicsql.expressions.SetExpression;
import org.hotrod.runtime.dynamicsql.expressions.TrimExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhenExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhereExpression;

public class TestSimpleSQL {

  private static final DynamicExpression EXPRESSION = //
      new CollectionExpression(//
          new LiteralExpression("select * from account "), //
          new BindExpression("diff", "maxAmount != null && minAmount != null ? maxAmount - minAmount : 0"), //
          new IfExpression( //
              "   minAmount != null", //
              new LiteralExpression(" and amount >= #minAmount#") //
          ), //
          new IfExpression( //
              "maxAmount != null", //
              new LiteralExpression(" and amount <= #maxAmount#") //
          ), //
          new IfExpression( //
              "diff > 15", //
              new LiteralExpression(" and type = 'HUGE'") //
          ), //
          new ChooseExpression( //
              new OtherwiseExpression( //
                  new LiteralExpression(" and region = 'CENTER'") //
              ), //
              new WhenExpression("kind == 1", new LiteralExpression(" and region = 'NORTH'")), //
              new WhenExpression("kind == 2", new LiteralExpression(" and region = 'SOUTH'")) //
          ), //
          new TrimExpression( //
              "where ", "and |or ", null, null, //
              new IfExpression("minAmount != null", new LiteralExpression(" And amount >= minAmount")), //
              new IfExpression("maxAmount != null", new LiteralExpression(" AnD amount <= maxAmount")) //
          ), //
          new TrimExpression( //
              null, null, "", " and| or", //
              new IfExpression("minAmount != null", new LiteralExpression("amount >= minAmount aNd ")), //
              new IfExpression("maxAmount != null", new LiteralExpression("amount <= maxAmount oR ")) //
          ), //
          new WhereExpression( //
              new IfExpression("minAmount != null", new LiteralExpression(" And amount >= minAmount")), //
              new IfExpression("maxAmount != null", new LiteralExpression(" AnD amount <= maxAmount")) //
          ), //
          new SetExpression( //
              new IfExpression("minAmount != null", new LiteralExpression(" And amount >= minAmount, ")), //
              new IfExpression("maxAmount != null", new LiteralExpression(" AnD amount <= maxAmount, ")) //
          ) //
      );

  public static void main(final String[] args) throws DynamicSQLEvaluationException {

    // evaluate();
    renderConstructor();
//    evaluateExpression();

  }

  private static void evaluate() throws DynamicSQLEvaluationException {
    DynamicSQLParameters parameters = new DynamicSQLParameters();
    boolean[] values = { false, true };

    for (boolean minPresent : values) {
      for (boolean maxPresent : values) {
        parameters.set("minAmount", minPresent ? 13 : null);
        parameters.set("maxAmount", maxPresent ? 23 : null);
        parameters.set("kind", null);
        System.out.println(EXPRESSION.evaluate(parameters));
      }
    }
  }

  private static void renderConstructor() {
    System.out.println(EXPRESSION.renderConstructor(0));
  }
  
  private static void evaluateExpression() throws DynamicSQLEvaluationException {
    DynamicExpression expr =
        new org.hotrod.runtime.dynamicsql.expressions.CollectionExpression(
            new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
              "select * from account "
            ),
            new org.hotrod.runtime.dynamicsql.expressions.BindExpression(
              "diff", "maxAmount != null && minAmount != null ? maxAmount - minAmount : 0"
            ),
            new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
              "minAmount != null",
              new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                " and amount >= #minAmount#"
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
              "maxAmount != null",
              new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                " and amount <= #maxAmount#"
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
              "diff > 15",
              new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                " and type = 'HUGE'"
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.ChooseExpression(
              new org.hotrod.runtime.dynamicsql.expressions.OtherwiseExpression(
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " and region = 'CENTER'"
                )
              ),
              new org.hotrod.runtime.dynamicsql.expressions.WhenExpression(
                "kind == 1",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " and region = 'NORTH'"
                )
              ),
              new org.hotrod.runtime.dynamicsql.expressions.WhenExpression(
                "kind == 2",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " and region = 'SOUTH'"
                )
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.TrimExpression(
              "where ", "'and '|'or '", null, "",
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "minAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " And amount >= minAmount"
                )
              ),
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "maxAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " AnD amount <= maxAmount"
                )
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.TrimExpression(
              null, "", " ]", "' and'|' or'",
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "minAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  "amount >= minAmount aNd "
                )
              ),
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "maxAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  "amount <= maxAmount oR "
                )
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.WhereExpression(
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "minAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " And amount >= minAmount"
                )
              ),
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "maxAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " AnD amount <= maxAmount"
                )
              )
            ),
            new org.hotrod.runtime.dynamicsql.expressions.SetExpression(
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "minAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " And amount >= minAmount, "
                )
              ),
              new org.hotrod.runtime.dynamicsql.expressions.IfExpression(
                "maxAmount != null",
                new org.hotrod.runtime.dynamicsql.expressions.LiteralExpression(
                  " AnD amount <= maxAmount, "
                )
              )
            )
          )
        ;
    
    DynamicSQLParameters parameters = new DynamicSQLParameters();
    parameters.set("minAmount", 13 );
    parameters.set("maxAmount", 23 );
    parameters.set("kind", null);
    System.out.println(expr.evaluate(parameters));
    
  }

}
