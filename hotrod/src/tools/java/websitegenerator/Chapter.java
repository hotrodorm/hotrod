package websitegenerator;

import java.util.TreeMap;

public class Chapter {

  private Integer chapterNumber;
  private TreeMap<Integer, Article> articles = new TreeMap<Integer, Article>();

  public Chapter(final Integer chapterNumber) {
    this.chapterNumber = chapterNumber;
  }

  public void addArticle(final Article a) throws DuplicateArticleException {
    if (this.articles.containsKey(a.getArticleNumber())) {
      throw new DuplicateArticleException(
          "Duplicate article " + this.chapterNumber + "." + a.getArticleNumber() + " for file: " + a.getFileName());
    }
    this.articles.put(a.getArticleNumber(), a);
  }

  public void renumber() {
    TreeMap<Integer, Article> newArticles = new TreeMap<Integer, Article>();
    int current = 10;
    for (Integer n : this.articles.keySet()) {
      Article a = this.articles.get(n);
      a.setArticleNumber(current);
      newArticles.put(a.getArticleNumber(), a);
    }
    this.articles = newArticles;
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
