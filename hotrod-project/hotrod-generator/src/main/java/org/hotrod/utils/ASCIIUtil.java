package org.hotrod.utils;

public final class ASCIIUtil {

  private ASCIIUtil() {
  }

  /**
   * Replaces every character outside the visible ASCII range (space through
   * tilde) with an underscore.
   *
   * @param value the value to sanitize
   * @return the sanitized value, or {@code null} when {@code value} is null
   */
  public static String sanitize(final String value) {
    if (value == null) {
      return null;
    }

    StringBuilder sanitized = new StringBuilder(value.length());
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      sanitized.append(c >= 0x20 && c <= 0x7e ? c : '_');
    }
    return sanitized.toString();
  }

}
