package org.hotrod.runtime.livesql.expressions.rendering;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.Separator;

public class Renderer {

  public static final void renderGenericFunctionTo(final String name, final String qualifier, final boolean includeParenthesis,
      final List<Expression> parameters, final QueryWriter w) {
    w.write(name);
    if (includeParenthesis) {
      w.write("(");
    } else {
      w.write(parameters.isEmpty() ? "" : " ");
    }
    if (qualifier != null) {
      w.write(qualifier + " ");
    }
    Separator sep = new Separator();
    for (Expression p : parameters) {
      w.write(sep.render());
      p.renderTo(w);
    }
    if (includeParenthesis) {
      w.write(")");
    }
  }

}
