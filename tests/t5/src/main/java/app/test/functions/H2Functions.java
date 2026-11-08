package app.test.functions;

import org.hotrod.livesql.expressions.Function;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.util.BoxUtil;
import org.springframework.stereotype.Component;

@Component
public class H2Functions {

  public NumericFunction random(NumericExpression x) {
    return Function.returnsNumeric(LayerResources.random, x);
  }

  public NumericFunction random(Number x) {
    return Function.returnsNumeric(LayerResources.random, BoxUtil.box(x));
  }

}
