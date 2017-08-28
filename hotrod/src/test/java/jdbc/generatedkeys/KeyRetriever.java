package jdbc.generatedkeys;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.JdbcUtils;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class KeyRetriever {

  public static void insert(final Connection conn, final DatabaseParticularities p, final String tableName,
      final Map<String, String> sequenceColumns, final List<String> identityColumns, final List<String> defaultColumns,
      final Map<String, String> dataColumns) throws SQLException {

    if (!sequenceColumns.isEmpty() && !p.combinesSequences()) {
      System.out.println("--- Cannot insert since this database (" + p.getName()
          + ") cannot retrieve sequences as part of the insert.");
      return;
    }

    if (!identityColumns.isEmpty() && !p.combinesIdentities()) {
      System.out.println("--- Cannot insert since this database (" + p.getName()
          + ") cannot retrieve identities as part of the insert.");
      return;
    }

    PreparedStatement st = null;
    CallableStatement cs = null;
    ResultSet rs = null;

    try {

      ListWriter names = new ListWriter(", ");
      ListWriter values = new ListWriter(", ");

      // Sequence columns (include sequences in-line expressions)

      for (String name : sequenceColumns.keySet()) {
        String value = p.inlineSequenceOnInsert(sequenceColumns.get(name));
        names.add(name);
        values.add(value);
      }

      // Identity columns (exclude identity columns)

      // Default columns (exclude default columns)

      // Data columns (include data values)

      for (String name : dataColumns.keySet()) {
        names.add(name);
        values.add("?");
      }

      // SQL

      String sql = "insert into " + tableName + " (" + names.toString() + ") values (" + values.toString() + ")";

      // System.out.println("p.getRetrievalType()=" + p.getRetrievalType());

      switch (p.getRetrievalType()) {

      case RETURN_GENERATED_KEYS_1: { // DB2, MySQL

        System.out.println("sql1=" + sql);

        st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int pos = 1;
        for (String name : dataColumns.keySet()) {
          st.setString(pos++, dataColumns.get(name));
        }

        int rows = st.executeUpdate();
        System.out.println("inserted rows=" + rows);
        // st.execute();

        rs = st.getGeneratedKeys();
        showKeys(rs);

        break;
      }

      case REQUEST_COLUMNS_2: { // DB2, MySQL, Oracle10g

        System.out.println("sql2=" + sql);

        List<String> generatedColumns = new ArrayList<String>();
        generatedColumns.addAll(sequenceColumns.keySet());
        generatedColumns.addAll(identityColumns);
        generatedColumns.addAll(defaultColumns);
        String[] array = generatedColumns.toArray(new String[0]);

        st = conn.prepareStatement(sql, array);

        int pos = 1;
        for (String name : dataColumns.keySet()) {
          st.setString(pos++, dataColumns.get(name));
        }

        int rows = st.executeUpdate();
        System.out.println("inserted rows=" + rows);

        rs = st.getGeneratedKeys();
        showKeys(rs);

        break;
      }

      case QUERY_RETURNING_COLUMNS_3: { // PostgreSQL

        List<String> generatedColumns = new ArrayList<String>();
        generatedColumns.addAll(sequenceColumns.keySet());
        generatedColumns.addAll(identityColumns);
        generatedColumns.addAll(defaultColumns);

        sql = sql + p.getReturningCoda(generatedColumns);

        System.out.println("sql3=" + sql);

        st = conn.prepareStatement(sql);

        int col = 1;
        for (String name : dataColumns.keySet()) {
          st.setString(col++, dataColumns.get(name));
        }

        rs = st.executeQuery();

        showKeys(rs);

        break;
      }

      default:

        System.out.println("No Retrieval type specified.");

      }

    } finally {
      JdbcUtils.closeDbResources(st, rs);
      JdbcUtils.closeDbResources(cs);
    }
  }

  // Utilities

  private static void showKeys(final ResultSet rs) throws SQLException {

    ResultSetMetaData rsmd = rs.getMetaData();
    int cols = rsmd.getColumnCount();
    System.out.println("columns=" + cols);

    boolean keysRetrieved = false;
    while (rs.next()) {
      keysRetrieved = true;
      System.out.print("keys: ");
      for (int i = 1; i <= cols; i++) {
        Long key = rs.getLong(i);
        // String key = rs.getString(i);
        System.out.print((i == 1 ? "" : ", ") + key + " ");
      }
      System.out.println();
    }

    if (!keysRetrieved) {
      System.out.println("No generated keys retrieved.");
    }
  }

}
