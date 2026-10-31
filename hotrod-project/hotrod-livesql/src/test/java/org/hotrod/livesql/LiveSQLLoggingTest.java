package org.hotrod.livesql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class LiveSQLLoggingTest {

  @Test
  void delegatesEnablementAndMessagesToConfiguredHandlers() {
    AtomicReference<String> basicMessage = new AtomicReference<>();
    AtomicReference<String> fullMessage = new AtomicReference<>();
    LiveSQLLogging logging = LiveSQLLogging.of(() -> true, basicMessage::set, () -> false, fullMessage::set);

    assertTrue(logging.basicEnabled());
    assertFalse(logging.fullEnabled());

    logging.basicLog("basic SQL");
    logging.fullLog("full SQL");

    assertEquals("basic SQL", basicMessage.get());
    assertEquals("full SQL", fullMessage.get());
  }
}