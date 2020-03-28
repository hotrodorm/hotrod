package websitegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import utils.FUtils;
import websitegenerator.Chapter.DuplicateArticleException;

public class ArticleAssembler {

  private File sourceDir;
  private File destDir;
  private File templateFile;

  public ArticleAssembler(final File sourceDir, final File destDir, final File templateFile) {
    this.sourceDir = sourceDir;
    this.destDir = destDir;
    this.templateFile = templateFile;
  }

  public void assembleArticles() throws DuplicateArticleException, CouldNotLoadTemplateException,
      CouldNotSaveArticleException, InvalidJavaIncludeException {

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
        try {
          book.add(name);
        } catch (FileNotFoundException e) {
          throw new CouldNotLoadTemplateException("Article file '" + name + "' not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
          throw new CouldNotLoadTemplateException(
              "Invalid article file encoding for file '" + name + "': " + e.getMessage());
        } catch (IOException e) {
          throw new CouldNotLoadTemplateException("Could not load article file '" + name + "': " + e.getMessage());
        }
      }
    }

    // Load the template

    String template = null;
    try {
      template = FUtils.loadFileAsString(this.templateFile);
    } catch (FileNotFoundException e) {
      throw new CouldNotLoadTemplateException("Template file not found: " + e.getMessage());
    } catch (UnsupportedEncodingException e) {
      throw new CouldNotLoadTemplateException("Invalid template file encoding: " + e.getMessage());
    } catch (IOException e) {
      throw new CouldNotLoadTemplateException("Could not load template file: " + e.getMessage());
    }

    // Produce assembled articles

    for (Chapter c : book.getChapters().values()) {
      for (Article a : c.getArticles().values()) {
        String filename = a.getFileName();
        String content = template;
        try {

          System.out.println("generating article '" + filename + "'...");

          String article = FUtils.loadFileAsString(new File(this.sourceDir, filename));

          // Add title

          String title = TitleFinder.findTitle(article, "(no title)");
          content = replaceToken(content, "${title}", title);

          // Add menu

          String menu = book.renderMenuFor(filename, title);
          content = replaceToken(content, "<menu />", menu);

          // Add article

          content = replaceToken(content, "<article />", article);

          // Include java code

          content = processJavaIncludes(content);

        } catch (FileNotFoundException e) {
          throw new CouldNotLoadTemplateException("Article file '" + filename + "' not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
          throw new CouldNotLoadTemplateException(
              "Invalid article file encoding for file '" + filename + "': " + e.getMessage());
        } catch (IOException e) {
          throw new CouldNotLoadTemplateException("Could not load article file '" + filename + "': " + e.getMessage());
        } catch (InvalidJavaIncludeException e) {
          throw new InvalidJavaIncludeException("Could not process article file '" + filename + "': " + e.getMessage());
        }

        // Save article

        try {

          FUtils.saveStringToFile(new File(this.destDir, filename), content);

        } catch (IOException e) {
          throw new CouldNotSaveArticleException("Could not save article '" + filename + "': " + e.getMessage());
        }

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
