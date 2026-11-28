package org.hotrod.livesql.functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.hotrod.utils.HexaUtils;

public final class FunctionUtils {

  static final int GCM_TAG_LENGTH = 128;
  static final SecretKeySpec ENCRYPTION_KEY = new SecretKeySpec(new byte[] { (byte) 0x62, (byte) 0x53, (byte) 0x37,
      (byte) 0x8e, (byte) 0xcc, (byte) 0x9b, (byte) 0x50, (byte) 0x3e, (byte) 0x97, (byte) 0x42, (byte) 0x35,
      (byte) 0x61, (byte) 0x69, (byte) 0xee, (byte) 0xb3, (byte) 0x4f, (byte) 0x65, (byte) 0x36, (byte) 0x66,
      (byte) 0x60, (byte) 0xef, (byte) 0x4b, (byte) 0x72, (byte) 0x3e, (byte) 0x74, (byte) 0x67, (byte) 0xd0,
      (byte) 0x8d, (byte) 0xa4, (byte) 0x5c, (byte) 0x53, (byte) 0xbf }, "AES");
  public static final byte[] NONCE = new byte[] { (byte) 0x3e, (byte) 0x4b, (byte) 0x55, (byte) 0x8d, (byte) 0x9f,
      (byte) 0x2a, (byte) 0xab, (byte) 0x7c, (byte) 0x6b, (byte) 0xef, (byte) 0x4a, (byte) 0x3f };
  static final int NONCE_LENGTH = NONCE.length;

  private FunctionUtils() {
  }

  public static LiveSQLFunctionTemplate prepare(final String hexa) {
    if (hexa == null) {
      return null;
    }

    final byte[] bytes;
    try {
      bytes = HexaUtils.toByteArray(hexa);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not decode hexadecimal serialized object", e);
    }

    try {
      return deserialize(decompress(decrypt(bytes)));
    } catch (IOException | GeneralSecurityException e) {
      throw new IllegalArgumentException("Could not deserialize hexadecimal object", e);
    }
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

  private static LiveSQLFunctionTemplate deserialize(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes))) {
      String name = readString(input);
      int segmentCount = input.readInt();
      if (segmentCount < 0) {
        throw new InvalidObjectException("A function template cannot contain a negative segment count");
      }
      TemplateSegment[] segments = new TemplateSegment[segmentCount];
      for (int i = 0; i < segmentCount; i++) {
        switch (input.readByte()) {
        case 'L':
          segments[i] = TemplateLiteral.of(readString(input));
          break;
        case 'A':
          segments[i] = TemplateArg.of();
          break;
        case 'V':
          String firstPrefix = readString(input);
          String prefix = readString(input);
          String suffix = readString(input);
          if (prefix == null || suffix == null) {
            throw new InvalidObjectException("A vararg segment requires a prefix and suffix");
          }
          segments[i] = TemplateVarArg.of(firstPrefix, prefix, suffix);
          break;
        default:
          throw new InvalidObjectException("Unknown function template segment type");
        }
      }
      if (input.available() != 0) {
        throw new InvalidObjectException("Unexpected data after function template");
      }
      return new LiveSQLFunctionTemplate(name, segments);
    } catch (IllegalArgumentException | IOException e) {
      throw new IllegalArgumentException("Could not deserialize function template", e);
    }
  }

  private static String readString(DataInputStream input) throws IOException {
    int length = input.readInt();
    if (length == -1) {
      return null;
    }
    if (length < -1 || length > input.available()) {
      throw new InvalidObjectException("Invalid function template string length");
    }
    byte[] bytes = new byte[length];
    input.readFully(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }
}