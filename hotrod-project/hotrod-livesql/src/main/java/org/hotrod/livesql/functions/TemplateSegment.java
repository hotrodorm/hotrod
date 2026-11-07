package org.hotrod.livesql.functions;

import java.util.Iterator;

import org.hotrod.livesql.expressions.ComparableExpression;

public abstract class TemplateSegment {

  abstract void prepare(Iterator<ComparableExpression> it, PreparedFunction preparingFunction);

}
