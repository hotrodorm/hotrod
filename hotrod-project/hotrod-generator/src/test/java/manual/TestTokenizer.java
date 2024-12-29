package manual;

import java.util.logging.Level;

import org.hotrod.config.dynamicsql.Tokenizer;
import org.hotrod.config.dynamicsql.Tokenizer.Token;
import org.hotrod.exceptions.InvalidConfigurationFileException;

public class TestTokenizer {

  static {
    JULCustomFormatter.initialize(Level.INFO);
  }

  public static void main(String[] args) throws InvalidConfigurationFileException {
    Tokenizer tokenizer = new Tokenizer(null, "#{asdasdasdas")  ;

    int i = 0;
    Token token;
    while ((token = tokenizer.next()) != null && i < 10) {
      System.out.println("> token: " + token.getType() + " - " + token.getBody());
      i++;
    }
  }

}
