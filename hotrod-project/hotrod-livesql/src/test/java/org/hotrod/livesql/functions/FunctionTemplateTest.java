package org.hotrod.livesql.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.IntegerLiteral;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.QueryWriter;
import org.junit.jupiter.api.Test;

class FunctionTemplateTest {

  @Test
  void rendersFunctionTemplateWithArgumentsAndVarargs() {

    // Define Bean

    LiveSQLFunctionTemplate t = LiveSQLFunctionTemplateBuilder //
        .begin("testfunction1") //
        .literal("substring(") //
        .arg() //
        .literal(",") //
        .arg() //
        .literal(")-[") //
        .vararg("(", ",(", ")") //
        .literal("]::int") //
        .end();

    // Prepare

    IntegerLiteral ZERO = IntegerLiteral.getZero();
    IntegerLiteral ONE = IntegerLiteral.getOne();
    IntegerLiteral TWO = IntegerLiteral.of(2);
    PreparedFunction preparedFunction = t.prepare(ZERO, ONE, TWO, ONE, ZERO);

    // Render

    LiveSQLContext context = new LiveSQLContext(null, null, null, null, null);
    QueryWriter w = new QueryWriter(context);

    preparedFunction.renderTo(w);

    LiveSQLPreparedQuery p = w.getPreparedQuery(null, false);
    String preview = p.getPreview(false);

    assertEquals("substring(0,1)-[(2),(1),(0)]::int", preview);
  }

  @Test
  void rendersLiteralOnlyTemplateWithoutArguments() {
    LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.begin("testfunction1").literal("current_date")
        .end();

    assertEquals("current_date", render(template));
  }

  @Test
  void rendersRegularArgumentsWithoutVarargs() {
    LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.begin("testfunction1") //
        .literal("pair(") //
        .arg() //
        .literal(",") //
        .arg() //
        .literal(")") //
        .end();

    assertEquals("pair(0,1)", render(template, IntegerLiteral.getZero(), IntegerLiteral.getOne()));
  }

  @Test
  void omitsVarargSectionWhenNoArgumentsRemain() {
    LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.begin("testfunction1") //
        .literal("values[") //
        .vararg("(", "),(", ")") //
        .literal("]") //
        .end();

    assertEquals("values[]", render(template));
  }

  @Test
  void rendersSingleVarargWithFirstPrefix() {
    LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.begin("testfunction1") //
        .literal("values[") //
        .vararg("(", "),(", ")") //
        .literal("]") //
        .end();

    assertEquals("values[(0)]", render(template, IntegerLiteral.getZero()));
  }

  @Test
  void rejectsInsufficientRegularArguments() {
    LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.begin("testfunction1").arg().arg().end();

    LiveSQLException exception = assertThrows(LiveSQLException.class, () -> template.prepare(IntegerLiteral.getZero()));

    assertEquals("Not enough arguments for the function template: testfunction1", exception.getMessage());
  }

  @Test
  void rejectsArgumentsRemainingAfterTemplatePreparation() {
    LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.begin("testfunction2").arg().end();

    LiveSQLException exception = assertThrows(LiveSQLException.class,
        () -> template.prepare(IntegerLiteral.getZero(), IntegerLiteral.getOne()));

    assertEquals("Too many arguments provided for function template: testfunction2", exception.getMessage());
  }

  @Test
  void rejectsNullVarargPrefixOrSuffix() {
    assertThrows(LiveSQLException.class,
        () -> LiveSQLFunctionTemplateBuilder.begin("testfunction1").vararg("(", null, ")"));
    assertThrows(LiveSQLException.class,
        () -> LiveSQLFunctionTemplateBuilder.begin("testfunction1").vararg("(", ",", null));
  }

  private String render(LiveSQLFunctionTemplate template, ComparableExpression... arguments) {
    PreparedFunction preparedFunction = template.prepare(arguments);
    QueryWriter writer = new QueryWriter(new LiveSQLContext(null, null, null, null, null));
    preparedFunction.renderTo(writer);
    return writer.getPreparedQuery(null, false).getPreview(false);
  }

}
