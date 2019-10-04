package org.hotrod.config;

public enum DisplayMode {

  SUMMARY, LIST;

  public static DisplayMode parse(final String txt) {
    for (DisplayMode dm : DisplayMode.values()) {
      if (dm.name().equalsIgnoreCase(txt)) {
        return dm;
      }
    }
    return null;
  }

}
