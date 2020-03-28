package org.hotrod.eclipseplugin.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialUtils {

  public static final byte[] serialize(final Object obj) throws IOException {
    ObjectOutputStream oos = null;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(100);
      oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();
      byte[] serialized = bos.toByteArray();
      return serialized;
    } finally {
      if (oos != null) {
        try {
          oos.close();
        } catch (IOException e) {
          // Ignore
        }
      }
    }
  }

  public static final Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(new ByteArrayInputStream(serialized));
      return ois.readObject();
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (IOException e) {
          // Ignore
        }
      }
    }
  }

  public static final Object deserialize(final InputStream is) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(is);
      return ois.readObject();
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (IOException e) {
          // Ignore
        }
      }
    }
  }

}
