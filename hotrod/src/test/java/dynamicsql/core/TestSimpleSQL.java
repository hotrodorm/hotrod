package dynamicsql.core;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.expressions.BindExpression;
import org.hotrod.runtime.dynamicsql.expressions.CollectionExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicSQLExpression;
import org.hotrod.runtime.dynamicsql.expressions.IfExpression;
import org.hotrod.runtime.dynamicsql.expressions.VerbatimExpression;
import org.hotrod.runtime.dynamicsql.expressions.WhereExpression;

public class TestSimpleSQL {

  private static final DynamicSQLExpression EXPRESSION = //
      new CollectionExpression(//
          new VerbatimExpression("select * from account "), //
          new BindExpression("diff", "maxAmount != null && minAmount != null ? maxAmount - minAmount : 0"), //
//          new IfExpression( //
//              "   minAmount != null", //
//              new VerbatimExpression(" and amount >= #minAmount#") //
//          ), //
//          new IfExpression( //
//              "maxAmount != null", //
//              new VerbatimExpression(" and amount <= #maxAmount#") //
//          ), //
//          new IfExpression( //
//              "diff > 15", //
//              new VerbatimExpression(" and type = 'HUGE'") //
//          ), //
//          new ChooseExpression( //
//              new CollectionExpression( // otherwise
//                  new VerbatimExpression(" and region = 'CENTER'") //
//              ), //
//              new WhenExpression("kind == 1", new VerbatimExpression(" and region = 'NORTH'")), //
//              new WhenExpression("kind == 2", new VerbatimExpression(" and region = 'SOUTH'")) //
//          ), //
//          new TrimExpression( //
//              "where ", "and |or ", null, null,
//              new IfExpression("minAmount != null", new VerbatimExpression(" And amount >= minAmount")),
//              new IfExpression("maxAmount != null", new VerbatimExpression(" AnD amount <= maxAmount"))
//          )
//          new TrimExpression( //
//              null, null, " ]", " and| or",
//              new IfExpression("minAmount != null", new VerbatimExpression("amount >= minAmount aNd ")),
//              new IfExpression("maxAmount != null", new VerbatimExpression("amount <= maxAmount oR "))
//          )
          new WhereExpression( //
            new IfExpression("minAmount != null", new VerbatimExpression(" And amount >= minAmount")), //
            new IfExpression("maxAmount != null", new VerbatimExpression(" AnD amount <= maxAmount")) //
          )
//          new SetExpression( //
//              new IfExpression("minAmount != null", new VerbatimExpression(" And amount >= minAmount, ")), //
//              new IfExpression("maxAmount != null", new VerbatimExpression(" AnD amount <= maxAmount, ")) //
//            )
      );

  public static void main(final String[] args) throws DynamicSQLEvaluationException {

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

}
