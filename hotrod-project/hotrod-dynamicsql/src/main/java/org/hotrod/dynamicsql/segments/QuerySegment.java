package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Parameters;

/**
 * <pre>
                     QuerySegment
                       ControlSegment
    .bind()              BindSegment     -- creates a "variable"
    .choose()            ChooseSegment
                         DynamicListSegment
    .set()                 SettersSegment
    .trim()                TrimSegment
    .where()               WhereSegment
    .forEach()           ForEachSegment
    .if_()               IfSegment
                         OtherwiseSegment
                         SetSegment
                           WhenSegment
                       ContentSegment
    .literal()           StaticContentSegment          --* String
                         DynamicContentSegment
    .parameter()           ParameterNotNullableSegment --* ParameterNotNullableInstance
    .parameterNullable()   ParameterNullableSegment    --* ParameterNullableInstance
    .parameterInjection()  ParameterInjectionSegment   --* String
    .variable()            VariableSegment             --* VariableInstance
    
                ParameterInstance
                  ParameterNotNullableInstance
                  ParameterNullableInstance
                  VariableInstance
                  ParameterNotNullChangeableInstance
    
    declared parameter
    declared variable
    declared injection

    prepared parameter
    prepared variable
    prepared injection

prepared ***
rendered **
expanded
extended
occurrence
interpreted
expressed
developed **
produced
fabricated
generated **
 * 
 * </pre>
 */

public abstract class QuerySegment {

  public abstract boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException;

}
