package org.hotrod.runtime.sql.dialects;

import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.sql.expressions.strings.StringExpression;

public interface FunctionRenderer {

  // String functions

  void concat(final QueryWriter w, List<StringExpression> strings);

  void length(final QueryWriter w, StringExpression string);

  void lower(final QueryWriter w, StringExpression string);

  void upper(final QueryWriter w, StringExpression string);

  void trim(final QueryWriter w, StringExpression string);

  void position(final QueryWriter w, StringExpression substring, StringExpression string, NumberExpression from);

  void substring(final QueryWriter w, StringExpression substring, NumberExpression from, NumberExpression length);

}
