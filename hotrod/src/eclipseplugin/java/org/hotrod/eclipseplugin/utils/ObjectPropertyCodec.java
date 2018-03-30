package org.hotrod.eclipseplugin.utils;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class ObjectPropertyCodec {

  private static final Logger log = Logger.getLogger(ObjectPropertyCodec.class);

  public static <T> TreeMap<Integer, String> encode(final T t, final int maxValueLength)
      throws CouldNotEncodeException {
    log.info("encode 1");
    TreeMap<Integer, String> parts = new TreeMap<Integer, String>();
    if (t == null) {
      return parts;
    }
    log.info("encode 2");
    byte[] blob;
    try {
      blob = SerialUtils.serialize(t);
    } catch (IOException e) {
      log.error("IO Exception", e);
      throw new CouldNotEncodeException(e.getMessage());
    }
    log.info("encode 3");
    String encoded = Base64.encodeBytes(blob);
    List<String> slices = SUtil.slice(encoded, maxValueLength);
    int index = 0;
    log.info("encode 4");
    for (String slice : slices) {
      parts.put(index, slice);
      index++;
    }
    log.info("encode 5");
    return parts;
  }

  public static <T> T decode(final TreeMap<Integer, String> parts, final Class<T> c) throws CouldNotDecodeException {
    StringBuilder sb = new StringBuilder();
    for (Integer index : parts.keySet()) {
      String value = parts.get(index);
      if (value != null) {
        sb.append(value.trim());
      }
    }
    String encoded = sb.toString();
    if (encoded.isEmpty()) {
      return null;
    } else {
      try {
        byte[] blob = Base64.decode(encoded);
        Object obj = SerialUtils.deserialize(blob);
        return c.cast(obj);
      } catch (IOException e) {
        throw new CouldNotDecodeException(e.getMessage());
      } catch (ClassNotFoundException e) {
        throw new CouldNotDecodeException(e.getMessage());
      } catch (ClassCastException e) {
        throw new CouldNotDecodeException(e.getMessage());
      }
    }
  }

  public static class CouldNotDecodeException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotDecodeException(final String message) {
      super(message);
    }

  }

  public static class CouldNotEncodeException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotEncodeException(final String message) {
      super(message);
    }

  }

}
