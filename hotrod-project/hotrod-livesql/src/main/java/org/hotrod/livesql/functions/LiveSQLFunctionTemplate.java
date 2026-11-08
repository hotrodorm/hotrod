package org.hotrod.livesql.functions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;

public class LiveSQLFunctionTemplate implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private TemplateSegment[] segments;
  private boolean sealed = false;

  public LiveSQLFunctionTemplate(String name, TemplateSegment[] segments) {
    this.name = name;
    this.segments = segments;
    for (TemplateSegment ts : this.segments) {
      ts.setTemplate(this);
    }
  }

  public String getName() {
    return this.name;
  }

  public PreparedFunction prepare(ComparableExpression... arguments) {
    Iterator<ComparableExpression> it = Arrays.asList(arguments).iterator();
    PreparedFunction preparingFunction = new PreparedFunction(this.name);
    for (TemplateSegment ts : this.segments) {
      ts.prepare(it, preparingFunction);
    }
    if (it.hasNext()) {
      throw new LiveSQLException("Too many arguments provided for function template: " + this.name);
    }
    return preparingFunction;
  }

}
