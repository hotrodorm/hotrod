package app.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfigInterface;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.springframework.stereotype.Component;

@Component
public class LayerConfiguration implements LayerConfigInterface {
  
  public LayerConfiguration () {
    System.out.println("$$$$$ LayerConfiguration! $$$$$$$");
  }

  @Override
  public List<TypeRule> getTypeRules() {
    List<TypeRule> rules = new ArrayList<>();

    rules.add(TypeRule.of("precision < 2", TypeHandler.forClass(Byte.class, TypeSource.LIVESQL_RULES), 1));
    rules.add(TypeRule.of("scale > 0", TypeHandler.forClass(Short.class, TypeSource.LIVESQL_RULES), 2));

    return rules;
  }

}
