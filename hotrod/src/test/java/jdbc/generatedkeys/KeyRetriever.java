package jdbc.generatedkeys;

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
      final Map<String, String> sequenceColumns, final List<String> identityColumns,
      final Map<String, String> dataColumns) throws SQLException {

    if (!sequenceColumns.isEmpty() && !p.canRetrieveSequencesAsPartOfTheInsert()) {
      System.out.println("--- Cannot insert since this database (" + p.getName()
          + ") cannot retrieve sequences as part of the insert.");
      return;
    }

    if (!identityColumns.isEmpty() && !p.canRetrieveIdentitiesAsPartOfTheInsert()) {
      System.out.println("--- Cannot insert since this database (" + p.getName()
          + ") cannot retrieve identities as part of the insert.");
      return;
    }

    PreparedStatement st = null;
    ResultSet rs = null;

    try {

      ListWriter names = new ListWriter(", ");
      ListWriter values = new ListWriter(", ");

      // Sequences columns

      // System.out.println("sequenceColumns.isEmpty()=");
      // System.out.println("p.canRetrieveSequencesAsPartOfTheInsert()=" +
      // p.canRetrieveSequencesAsPartOfTheInsert());
      for (String name : sequenceColumns.keySet()) {
        String value = p.inlineSequenceOnInsert(sequenceColumns.get(name));
        names.add(name);
        values.add(value);
      }

      // Identities columns

      // Data columns

      for (String name : dataColumns.keySet()) {
        names.add(name);
        values.add("?");
      }

      // SQL

      String sql = "insert into " + tableName + " (" + names.toString() + ") values (" + values.toString() + ")";

      switch (p.getPostRetrievalType()) {

      case GET_GENERATED_KEYS:

        System.out.println("sql1=" + sql);

        st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int pos = 1;
        for (String name : dataColumns.keySet()) {
          st.setString(pos++, dataColumns.get(name));
        }

        int rows = st.executeUpdate();
        System.out.println("inserted rows=" + rows);

        rs = st.getGeneratedKeys();
        showKeys(rs);

        break;

      case AS_QUERY:

        List<String> generatedColumns = new ArrayList<String>();
        generatedColumns.addAll(sequenceColumns.keySet());
        generatedColumns.addAll(identityColumns);

        sql = sql + p.getAsQueryCoda(generatedColumns);

        System.out.println("sql2=" + sql);

        st = conn.prepareStatement(sql);

        int col = 1;
        for (String name : dataColumns.keySet()) {
          st.setString(col++, dataColumns.get(name));
        }

        rs = st.executeQuery();

        showKeys(rs);

        break;

      default:

        System.out.println("No Retrieval type specified.");

      }

    } finally {
      JdbcUtils.closeDbResources(st, rs);
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
        System.out.print((i == 1 ? "" : ", ") + key + " ");
      }
      System.out.println();
    }

    if (!keysRetrieved) {
      System.out.println("No generated keys retrieved.");
    }
  }

}
