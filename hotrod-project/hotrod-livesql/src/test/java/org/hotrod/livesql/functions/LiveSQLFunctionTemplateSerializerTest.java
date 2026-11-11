package org.hotrod.livesql.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LiveSQLFunctionTemplateSerializerTest {

  @Test
  public void equivalentTemplatesAlwaysSerializeToTheSameResult() {
    LiveSQLFunctionTemplate first = template();
    LiveSQLFunctionTemplate second = template();

    String firstSerialized = FunctionUtils.render(first);
    String secondSerialized = FunctionUtils.render(second);

    assertEquals(firstSerialized, secondSerialized);
    assertEquals(firstSerialized, FunctionUtils.render(first));
  }

  @Test
  public void serializedTemplateCanBePreparedAgain() {
    LiveSQLFunctionTemplate original = template();
    String serialized = FunctionUtils.render(original);

    LiveSQLFunctionTemplate prepared = FunctionUtils.prepare(serialized);

    assertNotNull(prepared);
    assertEquals(original.getName(), prepared.getName());
    assertEquals(serialized, FunctionUtils.render(prepared));
  }

  @Test
  public void nullValuesAreHandledGracefully() {
    assertNull(FunctionUtils.render(null));
    assertNull(FunctionUtils.prepare(null));
  }

  @Test
  public void emptyTemplatesCanBeSerializedAndPrepared() {
    LiveSQLFunctionTemplate original = new LiveSQLFunctionTemplate("empty", new TemplateSegment[0]);
    String serialized = FunctionUtils.render(original);

    LiveSQLFunctionTemplate prepared = FunctionUtils.prepare(serialized);

    assertEquals(original.getName(), prepared.getName());
    assertEquals(serialized, FunctionUtils.render(prepared));
  }

  @Test
  public void malformedHexadecimalIsRejected() {
    IllegalArgumentException oddLength = assertThrows(IllegalArgumentException.class,
        () -> FunctionUtils.prepare("0"));
    IllegalArgumentException invalidCharacter = assertThrows(IllegalArgumentException.class,
        () -> FunctionUtils.prepare("zz"));

    assertEquals("Could not decode hexadecimal serialized object", oddLength.getMessage());
    assertEquals("Could not decode hexadecimal serialized object", invalidCharacter.getMessage());
  }

  @Test
  public void encryptedPayloadThatIsTooShortIsRejected() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> FunctionUtils.prepare("00"));

    assertEquals("Encrypted payload is too short", exception.getMessage());
  }

  @Test
  public void corruptedEncryptedPayloadIsRejected() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> FunctionUtils.prepare("00000000000000000000000000"));

    assertEquals("Could not deserialize hexadecimal object", exception.getMessage());
  }

  private static LiveSQLFunctionTemplate template() {
    return new LiveSQLFunctionTemplate("concat", new TemplateSegment[] {
        TemplateLiteral.of("préfix("),
        TemplateArg.of(),
        TemplateVarArg.of(null, ", ", ")"),
        TemplateLiteral.of("::suffix")
    });
  }

}
