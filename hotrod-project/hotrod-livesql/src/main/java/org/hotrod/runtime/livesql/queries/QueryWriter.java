package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.TypeHandler;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.QueryParameter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;
import org.hotrodorm.hotrod.utils.SUtil;

public class QueryWriter {

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
    if (context.usePlainJDBC()) {
      this.paramWriter = new JDBCParameterWriter();
    } else {
      this.paramWriter = new MyBatisParameterWriter();
    }
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
      Helper.renderTo(expression, this);
    }
  }

  public LiveSQLDialect getSQLDialect() {
    return this.context.getLiveSQLDialect();
  }

  public LiveSQLPreparedQuery getPreparedQuery(final List<Expression> columns) {
//    log.info("columns=" + columns + (columns != null ? " [" + columns.size() + "]" : ""));
    LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
    for (QueryParameter p : this.paramWriter.getParameters()) {
      params.put(p.getName(), p.getValue());
    }
    LinkedHashMap<String, Expression> queryColumns = null;
    if (columns != null) {
      queryColumns = new LinkedHashMap<>();
      int ordinal = 1;
      for (Expression c : columns) {
        String name = SUtil.coalesce(c.getReferenceName(), c.getProperty());
//        log.info("## name: " + name);
        if (name == null) {
          throw new LiveSQLException("Column #" + ordinal + " of the SELECT query does not have a name. "
              + "Please apply the .as() method to this expression to assign a name to it.");
        }
        if (queryColumns.containsKey(name)) {
          throw new LiveSQLException("Multiple query columns for this SELECT have the same name '" + name
              + "'. Please change the name of them to ensure all column names are distinct.");
        }
        queryColumns.put(name, c);
        ordinal++;
      }
    }
    log.info("queryColumns[" + (queryColumns == null?"null":queryColumns.size()) + "]");
    return new LiveSQLPreparedQuery(this.sb.toString(), params, queryColumns);
  }

  // Prepared Query

  public static class LiveSQLPreparedQuery {

    private String sql;
    private LinkedHashMap<String, Object> parameters;
    private LinkedHashMap<String, Expression> queryColumns;

    public LiveSQLPreparedQuery(final String sql, final LinkedHashMap<String, Object> parameters,
        final LinkedHashMap<String, Expression> queryColumns) {
      this.sql = sql;
      this.parameters = parameters;
      this.queryColumns = queryColumns;
    }

    public String getSQL() {
      return sql;
    }

    public LinkedHashMap<String, Object> getParameters() {
      return parameters;
    }

    public LinkedHashMap<String, Expression> getQueryColumns() {
      return queryColumns;
    }

    public LinkedHashMap<String, Object> getConsolidatedParameters() {
      LinkedHashMap<String, Object> c = new LinkedHashMap<String, Object>();
      c.putAll(this.parameters);
      c.put("sql", this.sql);
      return c;
    }

    // TODO: Clean up
//    @Deprecated // UsSe PreviewRenderer instead
//    public String render() {
//      log.info("render #1");
//      StringBuilder sb = new StringBuilder();
//      sb.append("--- SQL ---\n");
//      sb.append(this.sql);
//      sb.append("\n--- Parameters... ---\n");
//      for (String name : this.parameters.keySet()) {
//        Object value = this.getParameters().get(name);
//        sb.append(" * " + name + (value == null ? "" : " (" + value.getClass().getName() + ")") + ": " + value + "\n");
//      }
//      sb.append("\n--- Query columns ---\n");
//      for (String name : this.queryColumns.keySet()) {
//        Expression expr = this.queryColumns.get(name);
//        TypeHandler th = Helper.getTypeHandler(expr);
//        log.info("render: " + name + " th=" + th);
//        sb.append(" * " + name + ": " + th);
//      }
//      sb.append("------------------\n");
//      return sb.toString();
//    }

  }

}
