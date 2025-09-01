package websitegenerator.pdfmanual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.hotrod.runtime.util.ListWriter;

import com.itextpdf.text.DocumentException;

import utils.FUtils;
import websitegenerator.Article;
import websitegenerator.Book;
import websitegenerator.Chapter;
import websitegenerator.Chapter.DuplicateArticleException;
import websitegenerator.TitleFinder;

public class PDFArticleAssembler {

  private static final String TOOL_NAME = "HotRod";

  private File sourceDir;
  private File destDir;

  public PDFArticleAssembler(final File sourceDir, final File destDir) {
    this.sourceDir = sourceDir;
    this.destDir = destDir;
  }

  public void assembleArticles() throws DuplicateArticleException, InvalidJavaIncludeException, FileNotFoundException,
      UnsupportedEncodingException, IOException, DocumentException {

    // Load all articles

    String[] articles = this.sourceDir.list(new FilenameFilter() {
      @Override
      public boolean accept(final File dir, final String name) {
        return name.endsWith(".html");
      }
    });

    Book book = new Book(this.sourceDir);

    if (articles != null) {
      for (String name : articles) {
        book.add(name);
      }
    }

    // Produce assembled articles

    String separator = "<p style='page-break-before:always;'>&nbsp;</p>";
    ListWriter lw = new ListWriter(separator);

    for (Chapter c : book.getChapters().values()) {
      for (Article a : c.getArticles().values()) {
        String filename = a.getFileName();
        try {
          String article = FUtils.loadFileAsString(new File(this.sourceDir, filename));
          String title = TitleFinder.findTitle(article, "(no title)");
          article = processJavaIncludes(article);

          lw.add(article);

        } catch (InvalidJavaIncludeException e) {
          throw new InvalidJavaIncludeException("Could not process article file '" + filename + "': " + e.getMessage());
        }

      }
    }

    String content = lw.toString();

    ResourceProvider provider = new ResourceProvider(this.sourceDir);
    File out = new File(this.destDir, TOOL_NAME + "-Manual.pdf");

    OutputStream os = null;
    try {
      os = new FileOutputStream(out);

      PDFRenderer.renderPdf(content, os, provider);

    } finally {
      if (os != null) {
        os.close();
      }
    }

  }

  private static final String JAVA_BEGIN = "<java-file name=\"";
  private static final String JAVA_END = "\" />";

  private String processJavaIncludes(final String content) throws InvalidJavaIncludeException {
    int pos = 0;
    int start;
    int end;
    StringBuilder sb = new StringBuilder();
    while ((start = content.indexOf(JAVA_BEGIN, pos)) != -1) {
      end = content.indexOf(JAVA_END, start + JAVA_BEGIN.length());
      if (end == -1) {
        throw new InvalidJavaIncludeException("Invalid java include: initial token found, but no end token found.");
      }
      System.out
          .println("start=" + start + " end=" + end + " start + JAVA_BEGIN.length()=" + (start + JAVA_BEGIN.length()));
      String javaFileName = content.substring(start + JAVA_BEGIN.length(), end);
      File f = new File(javaFileName);
      String javaSource;
      try {
        javaSource = FUtils.loadFileAsString(f);
      } catch (FileNotFoundException e) {
        throw new InvalidJavaIncludeException(
            "Invalid java include: java file '" + f.getPath() + "' not found: " + e.getMessage());
      } catch (UnsupportedEncodingException e) {
        throw new InvalidJavaIncludeException(
            "Invalid java include: unsupported encoding on java file '" + f.getPath() + "': " + e.getMessage());
      } catch (IOException e) {
        throw new InvalidJavaIncludeException(
            "Invalid java include: could not read java file '" + f.getPath() + "': " + e.getMessage());
      }
      javaSource = javaSource.replaceAll("\\<", "\\&lt;");

      sb.append(content.substring(pos, start));
      sb.append("<pre class=\"java\">\n");
      sb.append(javaSource);
      sb.append("</pre>\n");

      pos = end + JAVA_END.length();
    }
    sb.append(content.substring(pos));

    return sb.toString();
  }

  // Utilities

  private String replaceToken(final String template, final String token, final String value) {
    int idx = template.indexOf(token);
    if (idx == -1) {
      return template;
    } else {
      return template.substring(0, idx) + value + template.substring(idx + token.length());
    }
  }

  public static class CouldNotLoadTemplateException extends Exception {

    private static final long serialVersionUID = 1L;

    public CouldNotLoadTemplateException(String message) {
      super(message);
    }

  }

  public static class CouldNotSaveArticleException extends Exception {

    private static final long serialVersionUID = 1L;

    public CouldNotSaveArticleException(String message) {
      super(message);
    }

  }

  public static class InvalidJavaIncludeException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidJavaIncludeException(String message) {
      super(message);
    }

  }

}
