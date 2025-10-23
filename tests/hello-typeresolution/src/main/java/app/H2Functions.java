package app;

import org.hotrod.livesql.expressions.Function;
import org.hotrod.livesql.expressions.object.ObjectFunction;
import org.springframework.stereotype.Component;

@Component
public class H2Functions {

  public ObjectFunction randomUUID() {
    return Function.returnsObject("RANDOM_UUID()");
  }

}
