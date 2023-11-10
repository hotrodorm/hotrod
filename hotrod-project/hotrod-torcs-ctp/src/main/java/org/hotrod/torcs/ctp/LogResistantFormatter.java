package org.hotrod.torcs.ctp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class LogResistantFormatter {

  private static final boolean USE_FAST_GZIP = true;
  private static final int LINE_SIZE = 120;

  public String[] format(final String plan) throws IOException {
    byte[] binaryHashed = toBinaryAndHash(plan);
    byte[] compressed = compress(binaryHashed, 5, USE_FAST_GZIP);
    String base64 = encodeBase64(compressed);
    String[] lines = splitAndShield(base64);
    System.out.println("binaryHashed[" + binaryHashed.length + "] compressed[" + compressed.length + "] base64["
        + base64.length() + "] lines[" + lines.length + "]");
    return lines;
  }

  private byte[] toBinaryAndHash(final String plan) throws IOException {
    byte[] binary = plan.getBytes(StandardCharsets.UTF_8);
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new IOException(e.getMessage());
    }
    messageDigest.update(binary);
    byte[] hash = messageDigest.digest();
    return concat(binary, hash);
  }

  private String encodeBase64(final byte[] compressed) {
    return Base64.getEncoder().encodeToString(compressed);
  }

  private String[] splitAndShield(final String base64) {
    String[] lines = splitBySize(base64, LINE_SIZE);
    String[] shielded = shield(lines);
    return shielded;
  }

  // Utilities

  public static byte[] compress(byte[] input, int compressionLevel, boolean GZIPFormat) throws IOException {
    Deflater compressor = new Deflater(compressionLevel, GZIPFormat);
    compressor.setInput(input);
    compressor.finish();
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    byte[] readBuffer = new byte[1024];
    int readCount = 0;
    while (!compressor.finished()) {
      readCount = compressor.deflate(readBuffer);
      if (readCount > 0) {
        bao.write(readBuffer, 0, readCount);
      }
    }
    compressor.end();
    return bao.toByteArray();
  }

  public static byte[] decompress(byte[] input, boolean GZIPFormat) throws IOException, DataFormatException {
    Inflater decompressor = new Inflater(GZIPFormat);
    decompressor.setInput(input);
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    byte[] readBuffer = new byte[1024];
    int readCount = 0;
    while (!decompressor.finished()) {
      readCount = decompressor.inflate(readBuffer);
      if (readCount > 0) {
        bao.write(readBuffer, 0, readCount);
      }
    }
    decompressor.end();
    return bao.toByteArray();
  }

  private byte[] concat(final byte[] a, final byte[] b) {
    byte[] combined = new byte[a.length + b.length];
    System.arraycopy(a, 0, combined, 0, a.length);
    System.arraycopy(b, 0, combined, a.length, b.length);
    return combined;
  }

  private String[] splitBySize(final String text, final int size) {
    String[] split = new String[(text.length() + size - 1) / size];
    int i = 0;
    for (int start = 0; start < text.length(); start += size) {
      split[i++] = text.substring(start, Math.min(text.length(), start + size));
    }
    return split;
  }

  private String[] shield(final String[] lines) {
    String[] shielded = new String[lines.length];
    for (int i = 0; i < lines.length; i++) {
      shielded[i] = "{" + (i + 1) + "/" + lines.length + ":" + lines[i] + "}";
    }
    return shielded;
  }

}
