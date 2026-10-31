package org.hotrod.livesql;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.hotrod.livesql.dialects.H2Dialect;
import org.hotrod.livesql.expressions.numeric.NumericConstant;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.junit.jupiter.api.Test;

class SelectArithmeticTest {

  @Test
  void rendersArithmeticOperatorsInTheSelectList() {
    LiveSQL liveSQL = liveSQL();

    String preview = liveSQL
        .select(new NumericConstant(10).plus(2).as("sum"), new NumericConstant(10).minus(2).as("difference"),
            new NumericConstant(10).mult(2).as("product"), new NumericConstant(10).div(2).as("quotient"))
        .from(liveSQL.DUAL).getPreview();

    System.out.println(preview);
    assertTrue(preview.contains("? + ?"));
    assertTrue(preview.contains("? - ?"));
    assertTrue(preview.contains("? * ?"));
    assertTrue(preview.contains("? / ?"));
    assertTrue(preview.contains(" as \"sum\""));
    assertTrue(preview.contains(" as \"difference\""));
    assertTrue(preview.contains(" as \"product\""));
    assertTrue(preview.contains(" as \"quotient\""));
  }

  @Test
  void preservesArithmeticPrecedenceAndRegistersSelectParameters() {
    LiveSQL liveSQL = liveSQL();

    String preview = liveSQL.select(new NumericConstant(10).plus(2).mult(3).as("product")).from(liveSQL.DUAL)
        .getPreview(true);

    System.out.println(preview);
    assertTrue(preview.contains("(? + ?) * ?"));
    assertTrue(preview.contains("--- Parameters (3) -------"));
    assertTrue(preview.contains(" as \"product\""));
  }

  private LiveSQL liveSQL() {
    return new LiveSQL(new H2Dialect(false, "H2", "2.2.224", 2, 2), null, null, new LayerConfiguration() {
      @Override
      public java.util.List<TypeRule> getRuntimeTypeSolverRules() {
        return Collections.emptyList();
      }

      @Override
      public String[] getAllowedPackages() {
        return new String[0];
      }
    });
  }
}