package org.hotrod.livesql;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GlobalSetupExtension implements BeforeAllCallback {

  private static boolean initialized = false;

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    System.out.println("INITIALIZING GLOBAL SETUP EXTENSION");
    if (!initialized) {
      System.out.println("Global setup before all tests");
      // Initialize resources here
      initialized = true;
    }
  }

}
