package org.hotrod.livesql.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    IllegalArgumentException oddLength = assertThrows(IllegalArgumentException.class, () -> FunctionUtils.prepare("0"));
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
    return new LiveSQLFunctionTemplate("concat", new TemplateSegment[] { TemplateLiteral.of("préfix("),
        TemplateArg.of(), TemplateVarArg.of(null, ", ", ")"), TemplateLiteral.of("::suffix") });
  }

  // Test Equivalence

  @Test
  public void equivalentPseudoRandomTemplatesAlwaysSerializeToTheSameResult() {
    Random r = new Random(20240620L);
    for (int i = 0; i < 1000; i++) {
      long seed = r.nextLong();
      eq1(seed);
    }
  }

  private void eq1(long seed) {
    LiveSQLFunctionTemplate first = randomTemplate(seed);
    LiveSQLFunctionTemplate second = randomTemplate(seed);

    String firstSerialized = FunctionUtils.render(first);
    String secondSerialized = FunctionUtils.render(second);

    assertEquals(firstSerialized, secondSerialized);
    assertEquals(firstSerialized, FunctionUtils.render(first));
  }

  private static LiveSQLFunctionTemplate randomTemplate(long seed) {
    Random r = new Random(seed);
    int count = r.nextInt(5) + 1; // Between 1 and 5 segments
    List<TemplateSegment> segments = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      segments.add(randomTemplateSegment(r));
    }

    return new LiveSQLFunctionTemplate(randomStringNotNull(r), segments.toArray(new TemplateSegment[0]));
  }

  public static TemplateSegment randomTemplateSegment(Random r) {
    int choice = r.nextInt(4);
    switch (choice) {
    case 0:
      return TemplateLiteral.of(randomString(r));
    case 1:
      return TemplateArg.of();
    case 2:
      return TemplateVarArg.of(randomString(r), randomStringNotNull(r), randomStringNotNull(r));
    case 3:
      return TemplateLiteral.of(randomString(r));
    default:
      throw new IllegalStateException("Unexpected value: " + choice);
    }
  }

  public static String randomString(Random r) {
    return randomString(r, true);
  }

  public static String randomStringNotNull(Random r) {
    return randomString(r, false);
  }

  public static String randomString(Random r, boolean allowNull) {
    if (allowNull && r.nextFloat() < 0.1) {
      return null;
    }
    int length = r.nextInt(10); // Length between 0 and 9
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char c = (char) (r.nextInt(26) + 'a'); // Random lowercase letter
      sb.append(c);
    }
    return sb.toString();
  }

}
