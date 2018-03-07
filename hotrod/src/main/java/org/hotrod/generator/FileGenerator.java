package org.hotrod.generator;

import java.io.File;
import java.io.IOException;

public interface FileGenerator {

  TextWriter createWriter(File f) throws IOException;

  public interface TextWriter {

    void write(String txt) throws IOException;

    void close() throws IOException;

  }
}
