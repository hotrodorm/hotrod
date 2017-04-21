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

  public void assembleArticles()
      throws DuplicateArticleException, CouldNotLoadTemplateException, CouldNotSaveArticleException {

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
        String name = a.getFilename();
        String content = template;
        try {

          System.out.println("generating article '" + name + "'...");

          String article = FUtils.loadFileAsString(new File(this.sourceDir, name));

          // Add title

          String title = TitleFinder.findTitle(article, "(no title)");
          content = replaceToken(content, "${title}", title);

          System.out.println("OK 1");

          // Add menu

          String menu = book.renderMenuFor(name, title);
          content = replaceToken(content, "<menu />", menu);

          System.out.println("OK 2");

          // Add article

          content = replaceToken(content, "<article />", article);

          System.out.println("OK 3");

        } catch (FileNotFoundException e) {
          throw new CouldNotLoadTemplateException("Article file '" + name + "' not found: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
          throw new CouldNotLoadTemplateException(
              "Invalid article file encoding for file '" + name + "': " + e.getMessage());
        } catch (IOException e) {
          throw new CouldNotLoadTemplateException("Could not load article file '" + name + "': " + e.getMessage());
        }

        // Save article

        try {

          FUtils.saveStringToFile(new File(this.destDir, name), content);

        } catch (IOException e) {
          throw new CouldNotSaveArticleException("Could not save article '" + name + "': " + e.getMessage());
        }

      }
    }

  }

  // Utilities

  private String replaceToken(final String template, final String token, final String value) {
    int idx = template.indexOf(token);
    if (idx == -1) {
      System.out.println("token '" + token + "' not found.");
      return template;
    } else {
      System.out.println(
          "replacing '" + token + "' by '" + (value.length() < 100 ? value : value.substring(0, 100) + "...") + "'");
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

}
