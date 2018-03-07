package org.hotrod.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.hotrod.generator.FileGenerator;

public class LocalFileGenerator implements FileGenerator {

  @Override
  public TextWriter createWriter(final File f) throws IOException {
    return new LocalTextWriter(f);
  }

  public static class LocalTextWriter implements TextWriter {

    private Writer w;

    public LocalTextWriter(final File f) throws IOException {
      this.w = new BufferedWriter(new FileWriter(f));
    }

    @Override
    public void write(final String txt) throws IOException {
      this.w.write(txt);
    }

    @Override
    public void close() throws IOException {
      this.w.close();
    }

  }

}
