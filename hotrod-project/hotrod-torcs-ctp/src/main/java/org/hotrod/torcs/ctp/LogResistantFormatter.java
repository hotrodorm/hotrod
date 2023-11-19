package org.hotrod.torcs.ctp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class LogResistantFormatter {

  private static final int DEFAULT_LINE_SIZE = 120;
  private static final int MIN_LINE_SIZE = 40;
  private static final int MAX_LINE_SIZE = 100000;

  private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";

  private static final boolean USE_FAST_GZIP = true;
  private static final int COMPRESSION_LEVEL = 5;

  private int payloadChunkSize;

  public LogResistantFormatter() {
    this.payloadChunkSize = DEFAULT_LINE_SIZE;
  }

  public LogResistantFormatter(final int payloadChunkSize) {
    this.payloadChunkSize = payloadChunkSize;
    if (payloadChunkSize < MIN_LINE_SIZE) {
      throw new IllegalArgumentException(
          "Line size must be greater or equal to " + MIN_LINE_SIZE + ", but it's " + payloadChunkSize + ".");
    }
    if (payloadChunkSize > MAX_LINE_SIZE) {
      throw new IllegalArgumentException(
          "Line size must less or equal to " + MAX_LINE_SIZE + ", but it's " + payloadChunkSize + ".");
    }
  }

  public String[] render(final String plan) throws IOException {
    byte[] binaryHashed = encodeAndHash(plan);
    byte[] compressed = compress(binaryHashed, COMPRESSION_LEVEL, USE_FAST_GZIP);
    String base64 = encodeBase64(compressed);
    String[] lines = splitAndShield(base64);
    System.out.println("binaryHashed[" + binaryHashed.length + "] compressed[" + compressed.length + "] base64["
        + base64.length() + "] lines[" + lines.length + "]");
    return lines;
  }

  private class Segment {

    private int line;
    private int total;
    private String content;

    public Segment(final int line, final int total, final String content) {
      this.line = line;
      this.total = total;
      this.content = content;
    }

    public int getLine() {
      return line;
    }

    public int getTotal() {
      return total;
    }

    public String getContent() {
      return content;
    }

    public String toString() {
      return "{" + this.line + "/" + this.total + ":" + this.content + "}";
    }

  }

  public String parse(final String[] logLines) throws InvalidLogDataException {
    List<Segment> segments = extractUnshield(logLines);
//    segments.stream().forEach(s -> System.out.println("> " + s));
    if (segments.isEmpty()) {
      throw new InvalidLogDataException("There are no log segments in this log.");
    }
    List<Segment> deduplicated = deduplicate(segments);
    String consolidated = validateSortConsolidate(deduplicated);
    byte[] compressed = decodeBase64(consolidated);
    byte[] hashed;
    try {
      hashed = decompress(compressed, USE_FAST_GZIP);
    } catch (IOException | DataFormatException e) {
      throw new InvalidLogDataException("Could not decompress data: " + e.getMessage());
    }
    String plan = validateHashRestore(hashed);
    return plan;
  }

  public static class InvalidLogDataException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidLogDataException(final String message) {
      super(message);
    }

  }

  private List<Segment> extractUnshield(final String[] logLines) {
    String log = Arrays.stream(logLines).collect(Collectors.joining());
    Pattern p = Pattern.compile("\\{([0-9]+)/([0-9]+)\\:([^\\}]*)}*");
    List<Segment> segments = new ArrayList<>();
    Matcher m = p.matcher(log);
    while (m.find()) {
      String line = m.group(1);
      String total = m.group(2);
      String content = m.group(3);
      segments.add(new Segment(Integer.valueOf(line), Integer.valueOf(total), content));
    }
    return segments;
  }

  private List<Segment> deduplicate(final List<Segment> segments) throws InvalidLogDataException {
    Map<Integer, Segment> byLine = new HashMap<>();
    for (Segment s : segments) {
      Segment existing = byLine.get(s.getLine());
      if (existing == null) {
        byLine.put(s.getLine(), s);
      } else {
        if (existing.getTotal() != s.getTotal()) {
          throw new InvalidLogDataException("Multiple segments found for line #" + s.getLine()
              + ", but with different total values (" + s.getTotal() + " and " + existing.getTotal() + ").");
        }
        if (!equalStrings(existing.getContent(), s.getContent())) {
          throw new InvalidLogDataException(
              "Multiple segments found for line #" + s.getLine() + ", but with different content.");
        }
      }
    }
    return new ArrayList<>(byLine.values());
  }

  private boolean equalStrings(final String a, final String b) {
    return a == null ? b == null : a.equals(b);
  }

  private String validateSortConsolidate(final List<Segment> segments) throws InvalidLogDataException {

    // 1. Validate Totals
    OptionalInt minTotal = segments.stream().mapToInt(s -> s.getTotal()).min();
    OptionalInt maxTotal = segments.stream().mapToInt(s -> s.getTotal()).max();
    if (minTotal.getAsInt() != maxTotal.getAsInt()) {
      throw new InvalidLogDataException(
          "All segments should have the same total value but there are different total values (between "
              + minTotal.getAsInt() + " and " + maxTotal.getAsInt() + ").");
    }

    // 2. Sort
    List<Segment> sorted = segments.stream().sorted((a, b) -> Integer.compare(a.getLine(), b.getLine()))
        .collect(Collectors.toList());

    // 3. Validate line numbers
    int i = 1;
    for (Iterator<Segment> it = sorted.iterator(); it.hasNext(); i++) {
      if (it.next().getLine() > i) {
        throw new InvalidLogDataException(
            "Missing segment #" + i + " out of " + minTotal.getAsInt() + " total segments");
      }
    }

    // 4. Consolidate
    return sorted.stream().map(s -> s.getContent()).collect(Collectors.joining());
  }

  private String validateHashRestore(final byte[] hashed) throws InvalidLogDataException {
    byte[] binary = Arrays.copyOfRange(hashed, 0, hashed.length - 32);
    byte[] hash = Arrays.copyOfRange(hashed, hashed.length - 32, hashed.length);
    byte[] digest;
    try {
      digest = digest(binary);
    } catch (IOException e) {
      throw new InvalidLogDataException("Could compute SHA-256 digest: " + e.getMessage());
    }
    if (!Arrays.equals(hash, digest)) {
      throw new InvalidLogDataException("Corrupted data: Invalid SHA-256 digest");
    }
    return new String(binary, StandardCharsets.UTF_8);
  }

  private byte[] encodeAndHash(final String data) throws IOException {
    byte[] binary = data.getBytes(StandardCharsets.UTF_8);
    byte[] digest = digest(binary);
    return concat(binary, digest);
  }

  private byte[] digest(byte[] binary) throws IOException {
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new IOException(e.getMessage());
    }
    messageDigest.update(binary);
    byte[] hash = messageDigest.digest();
    return hash;
  }

  private String encodeBase64(final byte[] decoded) {
    return Base64.getEncoder().encodeToString(decoded);
  }

  private byte[] decodeBase64(final String encoded) throws InvalidLogDataException {
    try {
      return Base64.getDecoder().decode(encoded);
    } catch (Exception e) {
      throw new InvalidLogDataException("Corrupted data: Could not decode Base64: " + e.getMessage());
    }
  }

  private String[] splitAndShield(final String base64) {
    String[] lines = splitBySize(base64, this.payloadChunkSize);
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
