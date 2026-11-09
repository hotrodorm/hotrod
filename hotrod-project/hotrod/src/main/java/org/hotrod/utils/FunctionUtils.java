package org.hotrod.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class FunctionUtils {

  private static final int NONCE_LENGTH = 12;
  private static final int GCM_TAG_LENGTH = 128;
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final SecretKeySpec ENCRYPTION_KEY = new SecretKeySpec(new byte[] { (byte) 0x62, (byte) 0x53,
      (byte) 0x37, (byte) 0x8e, (byte) 0xcc, (byte) 0x9b, (byte) 0x50, (byte) 0x3e, (byte) 0x97, (byte) 0x42,
      (byte) 0x35, (byte) 0x61, (byte) 0x69, (byte) 0xee, (byte) 0xb3, (byte) 0x4f, (byte) 0x65, (byte) 0x36,
      (byte) 0x66, (byte) 0x60, (byte) 0xef, (byte) 0x4b, (byte) 0x72, (byte) 0x3e, (byte) 0x74, (byte) 0x67,
      (byte) 0xd0, (byte) 0x8d, (byte) 0xa4, (byte) 0x5c, (byte) 0x53, (byte) 0xbf }, "AES");

  private FunctionUtils() {
  }

  private static byte[] compress(final byte[] bytes) throws IOException {
    try (ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(compressed) {
          {
            def.setLevel(Deflater.BEST_COMPRESSION);
          }
        }) {
      gzip.write(bytes);
      gzip.finish();
      return compressed.toByteArray();
    }
  }

  private static byte[] decompress(final byte[] bytes) throws IOException {
    try (ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        GZIPInputStream gzip = new GZIPInputStream(input);
        ByteArrayOutputStream decompressed = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[4096];
      int count;
      while ((count = gzip.read(buffer)) != -1) {
        decompressed.write(buffer, 0, count);
      }
      return decompressed.toByteArray();
    }
  }

  private static byte[] encrypt(final byte[] bytes) throws GeneralSecurityException, IOException {
    byte[] nonce = new byte[NONCE_LENGTH];
    RANDOM.nextBytes(nonce);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, ENCRYPTION_KEY, new GCMParameterSpec(GCM_TAG_LENGTH, nonce));
    byte[] encrypted = cipher.doFinal(bytes);

    ByteArrayOutputStream result = new ByteArrayOutputStream(NONCE_LENGTH + encrypted.length);
    result.write(nonce);
    result.write(encrypted);
    return result.toByteArray();
  }

  private static byte[] decrypt(final byte[] bytes) throws GeneralSecurityException {
    if (bytes.length <= NONCE_LENGTH) {
      throw new IllegalArgumentException("Encrypted payload is too short");
    }

    byte[] nonce = Arrays.copyOf(bytes, NONCE_LENGTH);
    byte[] encrypted = Arrays.copyOfRange(bytes, NONCE_LENGTH, bytes.length);
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, ENCRYPTION_KEY, new GCMParameterSpec(GCM_TAG_LENGTH, nonce));
    return cipher.doFinal(encrypted);
  }

  public static String render(final Object object) {
    if (object == null) {
      return null;
    }

    try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream objects = new ObjectOutputStream(bytes)) {
      objects.writeObject(object);
      objects.flush();
      return HexaUtils.toHexa(encrypt(compress(bytes.toByteArray())));
    } catch (IOException | GeneralSecurityException e) {
      throw new IllegalStateException("Could not serialize object", e);
    }
  }

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

    try (ByteArrayInputStream input = new ByteArrayInputStream(decompress(decrypt(bytes)));
        ObjectInputStream objects = new ObjectInputStream(input)) {
      return (T) objects.readObject();
    } catch (IOException | GeneralSecurityException | ClassNotFoundException e) {
      throw new IllegalArgumentException("Could not deserialize hexadecimal object", e);
    }
  }

}
