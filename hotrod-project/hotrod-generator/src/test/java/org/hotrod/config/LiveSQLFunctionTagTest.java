package org.hotrod.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.junit.jupiter.api.Test;

public class LiveSQLFunctionTagTest {

  @Test
  public void validJavaPropertyNameIsAccepted() {
    LiveSQLFunctionTag function = function("random_value2", "random({})");

    assertDoesNotThrow(() -> function.validate(null, null, null, null));
  }

  @Test
  public void invalidJavaPropertyNameIsRejected() {
    LiveSQLFunctionTag function = function("2random-value", "random({})");

    InvalidConfigurationFileException exception = assertThrows(InvalidConfigurationFileException.class,
        () -> function.validate(null, null, null, null));

    assertEquals(
        "The 'name' attribute of the <function> tag must start with a letter and continue with letters, digits, "
            + "and/or underscore symbols; however, '2random-value' was specified.",
        exception.getMessage());
  }

  private static LiveSQLFunctionTag function(final String name, final String template) {
    LiveSQLFunctionTag function = new LiveSQLFunctionTag();
    function.setName(name);
    function.setTemplate(template);
    return function;
  }

}
