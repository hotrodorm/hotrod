package sql.exceptions;

public class InvalidSQLClauseException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidSQLClauseException(String message) {
    super(message);
  }

}
