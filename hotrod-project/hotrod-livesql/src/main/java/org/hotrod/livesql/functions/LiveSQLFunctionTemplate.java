package org.hotrod.livesql.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;

public class LiveSQLFunctionTemplate {

  private String name;
  private List<TemplateSegment> templates = new ArrayList<>();
  private boolean sealed = false;

  public LiveSQLFunctionTemplate(String name) {
    this.name = name;
    this.templates = new ArrayList<>();
  }

  void add(TemplateSegment ts) {
    if (this.sealed) {
      throw new LiveSQLException("Cannot add segments to a sealed function template.");
    }
    this.templates.add(ts);
  }

  void seal() {
    this.sealed = true;
  }

  public String getName() {
    return this.name;
  }

  public PreparedFunction prepare(ComparableExpression... arguments) {
    Iterator<ComparableExpression> it = Arrays.asList(arguments).iterator();
    PreparedFunction preparingFunction = new PreparedFunction(this.name);
    for (TemplateSegment ts : this.templates) {
      ts.prepare(it, preparingFunction);
    }
    if (it.hasNext()) {
      throw new LiveSQLException("Too many arguments provided for function template: " + this.name);
    }
    return preparingFunction;
  }

}
