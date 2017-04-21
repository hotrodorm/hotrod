package websitegenerator;

import java.util.TreeMap;

public class Chapter {

  private Integer chapterNumber;
  private TreeMap<Integer, Article> articles = new TreeMap<Integer, Article>();

  public Chapter(final Integer chapterNumber) {
    this.chapterNumber = chapterNumber;
  }

  public void addArticle(final Integer articleNumber, final String name, final String title)
      throws DuplicateArticleException {
    if (this.articles.containsKey(articleNumber)) {
      throw new DuplicateArticleException(
          "Duplicate article " + this.chapterNumber + "." + articleNumber + " for file: " + name);
    }
    Article a = new Article(title, name);
    this.articles.put(articleNumber, a);
  }

  // Getters

  public Integer getChapterNumber() {
    return chapterNumber;
  }

  public TreeMap<Integer, Article> getArticles() {
    return articles;
  }

  // Utilities

  public static class DuplicateArticleException extends Exception {

    private static final long serialVersionUID = 1L;

    public DuplicateArticleException(String message) {
      super(message);
    }

  }

}
