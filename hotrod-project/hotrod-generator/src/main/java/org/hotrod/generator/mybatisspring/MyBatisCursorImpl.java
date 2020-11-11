package org.hotrod.generator.mybatisspring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.utils.ClassPackage;

public class MyBatisCursorImpl {

  private static final String CLASS_NAME = "MyBatisCursor";

  private DataSetLayout layout;

  private ClassPackage classPackage;
  private File f;

  public MyBatisCursorImpl(final DataSetLayout layout) {
    this.layout = layout;
    this.classPackage = this.layout.getDAOPrimitivePackage(null);
    File dir = this.layout.getDaoPrimitivePackageDir(null);
    this.f = new File(dir, CLASS_NAME + ".java");
  }

  public void generate() throws UncontrolledException {
    try (BufferedWriter w = new BufferedWriter(new FileWriter(this.f))) {
      println(w, "package " + this.classPackage.getPackage() + ";");
      println(w);
      println(w, "import java.io.IOException;");
      println(w, "import java.util.Iterator;");
      println(w);
      println(w, "import org.hotrod.runtime.cursors.Cursor;");
      println(w);
      println(w, "public class " + CLASS_NAME + "<T> implements Cursor<T> {");
      println(w);
      println(w, "  private org.apache.ibatis.cursor.Cursor<T> cursor;");
      println(w);
      println(w, "  public " + CLASS_NAME + "(final org.apache.ibatis.cursor.Cursor<T> cursor) {");
      println(w, "    this.cursor = cursor;");
      println(w, "  }");
      println(w);
      println(w, "  @Override");
      println(w, "  public Iterator<T> iterator() {");
      println(w, "    return this.cursor.iterator();");
      println(w, "  }");
      println(w);
      println(w, "  @Override");
      println(w, "  public void close() throws IOException {");
      println(w, "    this.cursor.close();");
      println(w, "  }");
      println(w);
      println(w, "}");
    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate the MyBatis cursor implementation: " + "could not write to file '" + this.f + "'.", e);
    }
  }

  public String getClassName() {
    return CLASS_NAME;
  }

  public String getFullClassName() {
    return this.classPackage.getFullClassName(CLASS_NAME);
  }

  private void println(final BufferedWriter w, final String line) throws IOException {
    w.write(line);
    w.newLine();
  }

  private void println(final BufferedWriter w) throws IOException {
    w.newLine();
  }

}
