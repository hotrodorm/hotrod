package org.hotrod.livesql.functions;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;

public class TemplateVarArg extends TemplateSegment {

  private static final long serialVersionUID = 1L;

  private String firstPrefix;
  private String prefix;
  private String suffix;

  private TemplateVarArg(String firstPrefix, String prefix, String suffix) {
    this.firstPrefix = firstPrefix;
    if (prefix == null) {
      throw new LiveSQLException("The prefix for vararg cannot be null in template"
          + (super.getTemplate() == null ? "" : ": " + super.getTemplate().getName()));
    }
    if (suffix == null) {
      throw new LiveSQLException("The suffix for vararg cannot be null in template"
          + (super.getTemplate() == null ? "" : ": " + super.getTemplate().getName()));
    }
    this.prefix = prefix;
    this.suffix = suffix;
  }

  static TemplateVarArg of(String firstPrefix, String prefix, String suffix) {
    return new TemplateVarArg(firstPrefix, prefix, suffix);
  }

  String getFirstPrefix() {
    return this.firstPrefix;
  }

  String getPrefix() {
    return this.prefix;
  }

  String getSuffix() {
    return this.suffix;
  }

  @Override
  void prepare(Iterator<ComparableExpression> it, PreparedFunction preparingFunction) {
    String actualPrefix = this.firstPrefix != null ? this.firstPrefix : this.prefix;
    while (it.hasNext()) {
      preparingFunction.add(PreparedLiteral.of(actualPrefix));
      ComparableExpression p = it.next();
      preparingFunction.add(PreparedArg.of(p));
      preparingFunction.add(PreparedLiteral.of(this.suffix));
      actualPrefix = this.prefix;
    }
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    if (this.prefix == null || this.suffix == null) {
      throw new InvalidObjectException("A vararg segment requires a prefix and suffix");
    }
  }

}