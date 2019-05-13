package org.hotrod.eclipseplugin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.hotrod.runtime.util.HexaUtils;

public class HashUtils {

  private static final byte[] NULL_DATA = new byte[1];
  static {
    NULL_DATA[0] = 123;
  }

  public static byte[] sha1(final byte[] data) throws NoSuchAlgorithmException {
    MessageDigest hasher = MessageDigest.getInstance("SHA-1");
    hasher.reset();
    hasher.update(data);
    return hasher.digest();
  }

  public static String hexaSha1(final String text) throws NoSuchAlgorithmException {
    byte[] data = text == null ? NULL_DATA : text.getBytes();
    byte[] hash = sha1(data);
    String hexa = HexaUtils.toHexa(hash);
    return hexa;
  }

}
