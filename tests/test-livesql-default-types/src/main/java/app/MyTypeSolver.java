package app;

import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyTypeSolver {

//  @Bean
//  public TypeSolver getTypeSolver() {
//    TypeSolver s = new TypeSolver();
//
//    s.addDialectRule(TypeRule.of("name == 'BAD'", "This is not a valid type 1."));
//
//    s.addUserRule(TypeRule.of("name == 'bad'", "This is not a valid type 2."));
//    s.addUserRule(TypeRule.of("true", TypeHandler.of(Integer.class)));
//    
//    return s;
//  }

}
