package websitegenerator;

public class TitleFinder {

  private static final String TITLE_INITIAL_DELIMITER = "<h1>";
  private static final String TITLE_END_DELIMITER = "</h1>";

  public static String findTitle(final String article, final String defaultValue) {
    System.out.println("findTitle()...");
    if (article == null) {
      return defaultValue;
    }
    int initial = article.indexOf(TITLE_INITIAL_DELIMITER);
    System.out.println("initial=" + initial);
    if (initial == -1) {
      return defaultValue;
    }
    int end = article.indexOf(TITLE_END_DELIMITER, initial + TITLE_INITIAL_DELIMITER.length());
    System.out.println("end=" + end);
    if (end == -1) {
      return defaultValue;
    }
    return article.substring(initial + TITLE_INITIAL_DELIMITER.length(), end);
  }

}
