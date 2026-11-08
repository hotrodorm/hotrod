package org.hotrod.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serializes Java objects to hexadecimal strings and restores them.
 *
 * <p>The serialized representation uses Java native serialization. Only
 * deserialize hexadecimal values from a trusted source; native Java
 * deserialization must not be used with attacker-controlled input.</p>
 */
public final class FunctionUtils {

  private FunctionUtils() {
  }

  /**
   * Serializes an object to a lowercase hexadecimal string.
   *
   * @param object the object to serialize; {@code null} is supported
   * @return the hexadecimal representation, or {@code null} when the object is
   *         {@code null}
   * @throws IllegalStateException if the object is not serializable or cannot
   *         be written
   */
  public static String render(final Object object) {
    if (object == null) {
      return null;
    }

    try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream objects = new ObjectOutputStream(bytes)) {
      objects.writeObject(object);
      objects.flush();
      return HexaUtils.toHexa(bytes.toByteArray());
    } catch (IOException e) {
      throw new IllegalStateException("Could not serialize object", e);
    }
  }

  /**
   * Deserializes an object from a hexadecimal string.
   *
   * <p>The input must have been produced by {@link #render(Object)} and
   * must come from a trusted source.</p>
   *
   * @param hexa the hexadecimal serialized object; {@code null} is supported
   * @param <T> the expected object type
   * @return the deserialized object, or {@code null} when the input is
   *         {@code null}
   * @throws IllegalArgumentException if the hexadecimal value is malformed or
   *         the serialized object cannot be read
   */
  @SuppressWarnings("unchecked")
  public static <T> T prepare(final String hexa) {
    if (hexa == null) {
      return null;
    }

    final byte[] bytes;
    try {
      bytes = HexaUtils.toByteArray(hexa);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not decode hexadecimal serialized object", e);
    }

    try (ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ObjectInputStream objects = new ObjectInputStream(input)) {
      return (T) objects.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new IllegalArgumentException("Could not deserialize hexadecimal object", e);
    }
  }

}
