package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.JDBCParameterOccurrence;
import org.hotrod.exceptions.InvalidConfigurationFileException;

public class NitroTokenizer {

  private static final Logger log = Logger.getLogger(NitroTokenizer.class.getName());

  private static final String INJECTION_PREFIX = "$SQLINJECTION{";
  private static final String INJECTION_SUFFIX = "}";

  public enum TokenType {
    LITERAL, SQL_PARAMETER, PARAMETER_INJECTION
  }

  private AbstractConfigurationTag tag;
  private String txt;
  private int pos;

  public NitroTokenizer(AbstractConfigurationTag tag, String txt) {
    log.fine("init");
    this.tag = tag;
    this.txt = txt;
    this.pos = 0;
  }

  public Token next() throws InvalidConfigurationFileException {
//    log.info("  pos=" + pos);
    if (this.pos >= this.txt.length()) {
      return null;
    }
    int sp = this.txt.indexOf(JDBCParameterOccurrence.PREFIX, pos);
    int ip = this.txt.indexOf(INJECTION_PREFIX, pos);
//    log.info("  sp=" + sp + " ip=" + ip);
    if (sp == -1) { // no SQL parameter
      if (ip == -1) { // literal
        return literal(this.txt.length());
      } else { // injection
        if (ip == pos) {
          return injection(ip);
        } else {
          return literal(ip);
        }
      }
    } else { // there's a SQL parameter
      if (ip == -1) { // SQL parameter
        if (sp == pos) {
          return sqlParameter(sp);
        } else {
          return literal(sp);
        }
      } else { // both found
        if (sp < ip) {
          if (sp == pos) {
            return sqlParameter(sp);
          } else {
            return literal(sp);
          }
        } else {
          if (ip == pos) {
            return injection(ip);
          } else {
            return literal(ip);
          }
        }
      }
    }
  }

  private Token literal(int end) {
    String body = this.txt.substring(this.pos, end);
    this.pos = end;
    return new Token(TokenType.LITERAL, body);
  }

  private Token injection(int ip) throws InvalidConfigurationFileException {
    int is = this.txt.indexOf(INJECTION_SUFFIX, ip + INJECTION_PREFIX.length());
    if (is == -1) {
      throw new InvalidConfigurationFileException(tag,
          "Unmatched parameter injection delimiters: the start '" + INJECTION_PREFIX
              + "' of the parameter injection was found, but not the end '" + INJECTION_SUFFIX + "' of it.");
    }
    String body = this.txt.substring(ip + INJECTION_PREFIX.length(), is);
    this.pos = is + INJECTION_SUFFIX.length();
    return new Token(TokenType.PARAMETER_INJECTION, body);
  }

  private Token sqlParameter(int sp) throws InvalidConfigurationFileException {
    int ss = this.txt.indexOf(JDBCParameterOccurrence.SUFFIX, sp + JDBCParameterOccurrence.PREFIX.length());
    if (ss == -1) {
      throw new InvalidConfigurationFileException(tag,
          "Unmatched SQL parameter delimiters: the start '" + JDBCParameterOccurrence.PREFIX
              + "' of the SQL parameter was found, but not the end '" + JDBCParameterOccurrence.SUFFIX + "' of it.");
    }
    String body = this.txt.substring(sp + JDBCParameterOccurrence.PREFIX.length(), ss);
    this.pos = ss + JDBCParameterOccurrence.SUFFIX.length();
    return new Token(TokenType.SQL_PARAMETER, body);
  }

  public class Token {

    private TokenType type;
    private String body;

    public Token(TokenType type, String body) {
      super();
      this.type = type;
      this.body = body;
    }

    public TokenType getType() {
      return type;
    }

    public String getBody() {
      return body;
    }

  }

}
