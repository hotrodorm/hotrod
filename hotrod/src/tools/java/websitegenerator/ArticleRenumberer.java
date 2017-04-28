package websitegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import websitegenerator.ArticleAssembler.CouldNotLoadTemplateException;
import websitegenerator.Chapter.DuplicateArticleException;

public class ArticleRenumberer {

  private File sourceDir;

  public ArticleRenumberer(final File sourceDir) {
    this.sourceDir = sourceDir;
  }

  public void renumber() throws CouldNotLoadTemplateException, DuplicateArticleException {

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
    
    

  }

}
