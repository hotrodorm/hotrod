package websitegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import utils.FUtils;
import websitegenerator.Chapter.DuplicateArticleException;

public class Book {

  private File sourceDir;
  private TreeMap<Integer, Chapter> chapters = new TreeMap<Integer, Chapter>();

  public Book(final File sourceDir) {
    this.sourceDir = sourceDir;
  }

  public void add(final String name)
      throws DuplicateArticleException, FileNotFoundException, UnsupportedEncodingException, IOException {
    ChapterArticleNumbers numbers = new ChapterArticleNumbers(name);
    Chapter c = this.chapters.get(numbers.getChapterNumber());
    if (c == null) {
      c = new Chapter(numbers.getChapterNumber());
      this.chapters.put(numbers.getChapterNumber(), c);
    }

    String article = FUtils.loadFileAsString(new File(this.sourceDir, name));

    // Add title

    String title = TitleFinder.findTitle(article, "(no title)");

    c.addArticle(numbers.getArticleNumber(), name, title);
  }

  public String renderMenuFor(final String name, final String title) {
    StringBuilder sb = new StringBuilder();

    sb.append("    <ul class=\"menu\">\n");

    // <li><a href="02-apache-2.0-license.html">Apache 2.0 License</a></li>
    // <li><a class="active" href="03-downloads.html">Downloads</a></li>

    for (Chapter c : this.chapters.values()) {
      for (Article article : c.getArticles().values()) {
        if (article.equals(name)) {
          sb.append("      <li><a class=\"active\" href=\"" + article.getFilename() + "\">" + article.getTitle()
              + "</a></li>\n");
        } else {
          sb.append("      <li><a href=\"" + article.getFilename() + "\">" + article.getTitle() + "</a></li>\n");
        }
      }
    }

    sb.append("    </ul>\n");

    return sb.toString();

  }

  // getters

  public TreeMap<Integer, Chapter> getChapters() {
    return chapters;
  }

  // extra

  private static class ChapterArticleNumbers {

    private Integer chapterNumber;
    private Integer articleNumber;

    public ChapterArticleNumbers(final String name) {

      if (name == null) {
        this.chapterNumber = 0;
        this.articleNumber = 0;
        return;
      }

      int dash = name.indexOf('-');
      if (dash == -1) {
        this.chapterNumber = 0;
        this.articleNumber = 0;
        return;
      }

      String numbers = name.substring(0, dash);
      int dot = numbers.indexOf('.');
      if (dot == -1) {
        this.chapterNumber = lenientParse(numbers);
        this.articleNumber = 0;
      } else {
        this.chapterNumber = lenientParse(numbers.substring(0, dot));
        this.articleNumber = lenientParse(numbers.substring(dot + 1));
      }
    }

    private Integer lenientParse(final String txt) {
      if (txt == null) {
        return 0;
      }
      try {
        return new Integer(txt);
      } catch (NumberFormatException e) {
        return 0;
      }
    }

    public Integer getChapterNumber() {
      return chapterNumber;
    }

    public Integer getArticleNumber() {
      return articleNumber;
    }

  }

}
