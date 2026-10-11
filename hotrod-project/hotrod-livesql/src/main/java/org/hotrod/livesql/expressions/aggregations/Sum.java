package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class Sum extends NumericAggregationUnfilteredFunction implements WindowableAggregationFunction {

  public Sum(final NumericExpression expression) {
    super("sum", expression);
  }

  
/**
 * 
Sum.java
SumDistinct.java

CountValuesDistinct.java
CountValues.java

Avg.java
AvgDistinct.java

NumericMax.java
NumericMin.java

---

CountRows.java

---

CharMax.java
CharMin.java

DateTimeMax.java
DateTimeMin.java

BooleanMax.java
BooleanMin.java

BinaryMax.java
BinaryMin.java

ObjectMax.java
ObjectMin.java

---

GroupConcat.java
GroupConcatDistinct.java
  
 */
  
}
