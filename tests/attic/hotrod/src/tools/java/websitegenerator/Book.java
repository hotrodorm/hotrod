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

  public void add(final String filename)
      throws DuplicateArticleException, FileNotFoundException, UnsupportedEncodingException, IOException {

    String articleContent = FUtils.loadFileAsString(new File(this.sourceDir, filename));
    String title = TitleFinder.findTitle(articleContent, "(no title)");

    Article a = new Article(filename, title);
    Chapter c = this.chapters.get(a.getChapterNumber());
    if (c == null) {
      c = new Chapter(a.getChapterNumber());
      this.chapters.put(a.getChapterNumber(), c);
    }

    c.addArticle(a);
  }

  public String renderMenuFor(final String filename, final String title) {
    StringBuilder sb = new StringBuilder();

    sb.append("    <ul class=\"menu\">\n");

    for (Chapter c : this.chapters.values()) {
      for (Integer articleNumber : c.getArticles().keySet()) {
        Article a = c.getArticles().get(articleNumber);
        String li = "<li" + (articleNumber.equals(0) ? "" : " class=\"menu2\"") + ">";
        if (a.getFileName().equals(filename)) {
          sb.append(
              "      " + li + "<a class=\"active\" href=\"" + a.getFileName() + "\">" + a.getTitle() + "</a></li>\n");
        } else {
          sb.append("      " + li + "<a href=\"" + a.getFileName() + "\">" + a.getTitle() + "</a></li>\n");
        }
      }
    }

    sb.append("    </ul>\n");

    return sb.toString();

  }

  public void renumber() {
    int next = 10;
    TreeMap<Integer, Chapter> newChapters = new TreeMap<Integer, Chapter>();
    for (Chapter c : this.chapters.values()) {
      c.setChapterNumber(next);
      next += 10;
      newChapters.put(c.getChapterNumber(), c);
      c.renumber();
    }
    this.chapters = newChapters;
  }

  // getters

  public TreeMap<Integer, Chapter> getChapters() {
    return chapters;
  }

}
