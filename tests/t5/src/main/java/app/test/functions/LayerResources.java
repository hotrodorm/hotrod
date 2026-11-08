package app.test.functions;

import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplateBuilder;

public class LayerResources {

  public static LiveSQLFunctionTemplate random = LiveSQLFunctionTemplateBuilder.name("random") //
      .literal("(random(123) * (") //
      .arg() //
      .literal("))") //
      .end();

}
