package org.hotrod.livesql.functions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;
import org.hotrod.livesql.functions.TemplateArg;
import org.hotrod.livesql.functions.TemplateLiteral;
import org.hotrod.livesql.functions.TemplateSegment;
import org.hotrod.livesql.functions.TemplateVarArg;
import org.hotrod.utils.HexaUtils;

public abstract class FunctionSerializer {

  private FunctionSerializer() {
  }

  // Compression

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

  // Encryption

  private static byte[] encrypt(final byte[] bytes) throws GeneralSecurityException, IOException {

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, FunctionUtils.ENCRYPTION_KEY,
        new GCMParameterSpec(FunctionUtils.GCM_TAG_LENGTH, FunctionUtils.NONCE));
    byte[] encrypted = cipher.doFinal(bytes);

    ByteArrayOutputStream result = new ByteArrayOutputStream(FunctionUtils.NONCE.length + encrypted.length);
    result.write(FunctionUtils.NONCE);
    result.write(encrypted);
    return result.toByteArray();
  }

  // Serialization

  private static byte[] serialize(LiveSQLFunctionTemplate template) {
    try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes)) {
      writeString(output, template.name);
      output.writeInt(template.segments.length);
      for (TemplateSegment segment : template.segments) {
        if (segment instanceof TemplateLiteral) {
          output.writeByte('L');
          writeString(output, ((TemplateLiteral) segment).getText());
        } else if (segment instanceof TemplateArg) {
          output.writeByte('A');
        } else if (segment instanceof TemplateVarArg) {
          TemplateVarArg varArg = (TemplateVarArg) segment;
          output.writeByte('V');
          writeString(output, varArg.getFirstPrefix());
          writeString(output, varArg.getPrefix());
          writeString(output, varArg.getSuffix());
        } else {
          throw new IllegalStateException("Unknown template segment type: " + segment.getClass().getName());
        }
      }
      output.flush();
      return bytes.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException("Could not serialize function template", e);
    }
  }

  private static void writeString(DataOutputStream output, String value) throws IOException {
    if (value == null) {
      output.writeInt(-1);
      return;
    }
    byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
    output.writeInt(bytes.length);
    output.write(bytes);
  }

  // Rendering

  public static String render(final LiveSQLFunctionTemplate template) {
    if (template == null) {
      return null;
    }
    try {
      return HexaUtils.toHexa(encrypt(compress(serialize(template))));
    } catch (IOException | GeneralSecurityException e) {
      throw new IllegalStateException("Could not serialize object", e);
    }
  }

}
