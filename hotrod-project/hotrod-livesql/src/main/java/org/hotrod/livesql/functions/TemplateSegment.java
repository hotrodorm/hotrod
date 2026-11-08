package org.hotrod.livesql.functions;

import java.io.Serializable;
import java.util.Iterator;

import org.hotrod.functions.Section;
import org.hotrod.livesql.expressions.ComparableExpression;

public abstract class TemplateSegment implements Serializable {

  private static final long serialVersionUID = 1L;

  private LiveSQLFunctionTemplate template;

  void setTemplate(LiveSQLFunctionTemplate template) {
    this.template = template;
  }

  protected LiveSQLFunctionTemplate getTemplate() {
    return this.template;
  }

  public static TemplateSegment of(Section s) {
    if (s instanceof org.hotrod.functions.LiteralSection) {
      return TemplateLiteral.of(((org.hotrod.functions.LiteralSection) s).getLiteral());
    } else if (s instanceof org.hotrod.functions.ArgSection) {
      return TemplateArg.of();
    } else if (s instanceof org.hotrod.functions.VarArgSection) {
      org.hotrod.functions.VarArgSection vas = (org.hotrod.functions.VarArgSection) s;
      return TemplateVarArg.of(vas.getFirstPrefix(), vas.getPrefix(), vas.getSuffix());
    } else {
      throw new IllegalArgumentException("Unknown section type: " + s.getClass().getName());
    }
  }

  abstract void prepare(Iterator<ComparableExpression> it, PreparedFunction preparingFunction);

}
