package app;

import org.hotrod.livesql.expressions.Function;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.springframework.stereotype.Component;

@Component
public class H2Functions {

  public BinaryFunction randomUUID() {
    return Function.returnsBinary("RANDOM_UUID()");
  }

}
