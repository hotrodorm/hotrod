package websitegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;

import websitegenerator.ArticleAssembler.CouldNotLoadTemplateException;
import websitegenerator.Chapter.DuplicateArticleException;

public class ArticleRenumberer {

  private File sourceDir;
  private File destDir;

  public ArticleRenumberer(final File sourceDir, final File destDir) {
    this.sourceDir = sourceDir;
    this.destDir = destDir;
  }

  public void renumber() throws CouldNotLoadTemplateException, DuplicateArticleException, CouldNotCopyFileException {

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

    // Renumber

    book.renumber();

    // Save the articles

    Integer maxChapter = null;
    for (Chapter c : book.getChapters().values()) {
      if (maxChapter == null || c.getChapterNumber() > maxChapter) {
        maxChapter = c.getChapterNumber();
      }
    }
    ArticleNumberFormatter cf = new ArticleNumberFormatter(maxChapter);

    for (Chapter c : book.getChapters().values()) {

      Integer maxArticle = null;
      for (Article a : c.getArticles().values()) {
        if (maxArticle == null || a.getArticleNumber() > maxArticle) {
          maxArticle = a.getArticleNumber();
        }
      }
      ArticleNumberFormatter af = new ArticleNumberFormatter(maxArticle);

      for (Article a : c.getArticles().values()) {
        File srcFile = new File(this.sourceDir, a.getFileName());
        File destFile = new File(this.destDir, a.produceFileName(cf, af, c.getArticles().size() > 1));
        try {
          FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
          throw new CouldNotCopyFileException(
              "Could not copy file '" + srcFile.getPath() + "' to '" + destFile.getPath() + "': " + e.getMessage());
        }
      }
    }
  }

  public static class CouldNotCopyFileException extends Exception {

    private static final long serialVersionUID = 1L;

    public CouldNotCopyFileException(String message) {
      super(message);
    }

  }

}
