package org.hotrod.livesql.queries;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.SQLParameterWriter.QueryParameter;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class QueryWriter {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(QueryWriter.class.getName());

  private static final String INDENT = "  "; // two spaces to indent each level

  private LiveSQLContext context;

  private StringBuilder sb;
  private int level;
  private int col;

  private SQLParameterWriter paramWriter;

  public QueryWriter(final LiveSQLContext context) {
    this.context = context;
    this.sb = new StringBuilder();
    this.level = 0;
    this.col = 0;
    this.paramWriter = new JDBCParameterWriter();
  }

  public RenderedParameter registerParameter(final Object value) {
    return this.paramWriter.registerParameter(value);
  }

  public void enterLevel() {
    this.level++;
  }

  public void exitLevel() {
    this.level--;
  }

  public int getLevel() {
    return this.level;
  }

  public void write(final String txt) {
    if (txt != null) {
      String[] lines = txt.split("\n");
      for (int i = 0; i < lines.length; i++) {
        String line = lines[i];
        if (i > 0) {
          newLine();
        }
        indent();
        this.sb.append(line);
        this.col = this.col + line.length();
      }
      if (txt.endsWith("\n")) {
        newLine();
      }
    }
  }

  private void newLine() {
    this.sb.append("\n");
    this.col = 0;
  }

  private void indent() {
    int missingIndents = (this.level * INDENT.length() - this.col) / INDENT.length();
    for (int i = 0; i < missingIndents; i++) {
      this.sb.append(INDENT);
      this.col = this.col + INDENT.length();
    }
  }

  public void write(final ComparableExpression expression) {
    if (expression != null) {
      Shield.renderTo(expression, this);
    }
  }

  public LiveSQLDialect getSQLDialect() {
    return this.context.getLiveSQLDialect();
  }

  public LiveSQLPreparedQuery getPreparedQuery(final List<Expression> columns, boolean excludeTuplesFromUniqueNames) {
    LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
//    log.info(">>> this.paramWriter.getParameters().size()=" + this.paramWriter.getParameters().size());
    log.info("excludeTuplesFromUniqueNames=" + excludeTuplesFromUniqueNames);
    for (QueryParameter p : this.paramWriter.getParameters()) {
      params.put(p.getName(), p.getValue());
    }
    Set<String> uniqueNames = null;
    if (columns != null) {
      uniqueNames = new HashSet<>();
      int ordinal = 1;
      for (Expression c : columns) {
        String name = Shield.getReferenceName(c);
        if (excludeTuplesFromUniqueNames && Shield.isEntityColumn(c)) {
          // Exclude from name uniqueness check
          log.info(">> excluding: " + name);
        } else {
          log.info(">> including: " + name);
          if (name == null) {
            throw new LiveSQLException("Column #" + ordinal + " of the SELECT query does not have a name. "
                + "Please apply the .as() method to this expression to assign a name to it.");
          }
          if (uniqueNames.contains(name)) {
            throw new LiveSQLException("There are multiple query columns with the same name '" + name
                + "' in the main SELECT list of this query. "
                + "Please change this list or use aliases to ensure all resulting column names are different.");
          }
          uniqueNames.add(name);
        }
        ordinal++;
      }
    }
    return new LiveSQLPreparedQuery(this.sb.toString(), params, columns);
  }

}
