package org.plan.test;

import java.util.Date;
import java.util.LinkedHashMap;

import org.plan.ExecutionPlan;
import org.plan.operator.Operator;
import org.plan.renderer.text.TextRenderer;

public class PlanTester {

  public static void main(final String[] args) {

    ExecutionPlan plan = retrievePlan();

    String report = TextRenderer.render(plan, false);

    System.out.println("Execution Plan Report");
    System.out.println();
    System.out.println(report);

  }

  private static ExecutionPlan retrievePlan() {
    LinkedHashMap<String, Object> parameterValues = new LinkedHashMap<String, Object>();
    parameterValues.put("id", new Integer(10));

    Operator rootOperator = null;

    ExecutionPlan plan = ExecutionPlan.instantiate("query-001", new Date(), "select *\nfrom account\nwhere id = #{id}",
        parameterValues, rootOperator, true, false);
    return plan;
  }

}
