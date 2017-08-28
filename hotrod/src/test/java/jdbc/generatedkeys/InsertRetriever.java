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

import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.JdbcUtils;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public enum InsertRetriever {

  RETURN_GENERATED_KEYS_1, //
  RETURN_COLUMN_NAMES_2, //
  RETURN_COLUMN_INDEXES_3, //
  QUERY_RETURNING_COLUMNS_4, //
  QUERY_SELECT_5, //
  QUERY_OUTPUT_6 //
  ;

  public void insert(final Connection conn, final DatabaseParticularities p, final Table t) throws SQLException {

    if (!t.getSequenceColumns().isEmpty() && !p.combinesSequences()) {
      System.out.println("--- Cannot insert since this database (" + p.getName()
          + ") cannot retrieve sequences as part of the insert.");
      return;
    }

    if (!t.getIdentityColumns().isEmpty() && !p.combinesIdentities()) {
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

      for (SequenceColumn s : t.getSequenceColumns()) {
        names.add(s.getName());
        values.add(p.inlineSequenceOnInsert(s.getSequence()));
      }

      // Identity columns (exclude identity columns)

      // Default columns (exclude default columns)

      // Data columns (include data values)

      for (DataColumn d : t.getDataColumns()) {
        names.add(d.getName());
        values.add("?");
      }

      // System.out.println("[TABLE] " + t);

      // SQL

      String sql = "insert into " + t.getTableName() + " (" + names.toString() + ") values (" + values.toString() + ")";

      switch (this) {

      case RETURN_GENERATED_KEYS_1: {

        System.out.println("[ sql1: " + sql + " ]");

        st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int pos = 1;
        for (DataColumn d : t.getDataColumns()) {
          st.setString(pos++, d.getValue());
        }

        int rows = st.executeUpdate();
        System.out.println("[ inserted rows=" + rows + " ]");

        rs = st.getGeneratedKeys();
        showKeys(rs);

        break;
      }

      case RETURN_COLUMN_NAMES_2: {

        System.out.println("[ sql2: " + sql + " ]");

        List<String> generatedColumnNames = new ArrayList<String>();
        for (SequenceColumn c : t.getSequenceColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (IdentityColumn c : t.getIdentityColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (DefaultColumn c : t.getDefaultColumns()) {
          generatedColumnNames.add(c.getName());
        }
        String[] columnNames = generatedColumnNames.toArray(new String[0]);
        System.out.println(
            "  --> Retrieving column names [" + columnNames.length + "]: " + ListWriter.render(columnNames, ", "));

        st = conn.prepareStatement(sql, columnNames);

        int pos = 1;
        for (DataColumn d : t.getDataColumns()) {
          st.setString(pos++, d.getValue());
        }

        int rows = st.executeUpdate();
        System.out.println("[ inserted rows=" + rows + " ]");

        rs = st.getGeneratedKeys();
        showKeys(rs);

        break;
      }

      case RETURN_COLUMN_INDEXES_3: {

        System.out.println("[ sql3: " + sql + " ]");

        List<Integer> generatedColumnIndexes = new ArrayList<Integer>();
        for (SequenceColumn c : t.getSequenceColumns()) {
          generatedColumnIndexes.add(c.getIndex());
        }
        for (IdentityColumn c : t.getIdentityColumns()) {
          generatedColumnIndexes.add(c.getIndex());
        }
        for (DefaultColumn c : t.getDefaultColumns()) {
          generatedColumnIndexes.add(c.getIndex());
        }
        int[] columnIndexes = new int[generatedColumnIndexes.size()];
        int i = 0;
        for (Integer index : generatedColumnIndexes) {
          columnIndexes[i++] = index;
        }

        st = conn.prepareStatement(sql, columnIndexes);

        int pos = 1;
        for (DataColumn d : t.getDataColumns()) {
          st.setString(pos++, d.getValue());
        }

        int rows = st.executeUpdate();
        System.out.println("[ inserted rows=" + rows + " ]");

        rs = st.getGeneratedKeys();
        showKeys(rs);

        break;
      }

      case QUERY_RETURNING_COLUMNS_4: {

        List<String> generatedColumnNames = new ArrayList<String>();
        for (SequenceColumn c : t.getSequenceColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (IdentityColumn c : t.getIdentityColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (DefaultColumn c : t.getDefaultColumns()) {
          generatedColumnNames.add(c.getName());
        }

        sql = sql + p.getReturningCoda(generatedColumnNames);

        System.out.println("[ sql4: " + sql + " ]");

        st = conn.prepareStatement(sql);

        int pos = 1;
        for (DataColumn d : t.getDataColumns()) {
          st.setString(pos++, d.getValue());
        }

        rs = st.executeQuery();
        System.out.println("[ insert executed ]");

        showKeys(rs);

        break;
      }

      case QUERY_SELECT_5: {

        List<String> generatedColumnNames = new ArrayList<String>();
        for (SequenceColumn c : t.getSequenceColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (IdentityColumn c : t.getIdentityColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (DefaultColumn c : t.getDefaultColumns()) {
          generatedColumnNames.add(c.getName());
        }

        sql = "select " + ListWriter.render(generatedColumnNames, ", ") + " from final table (\n " + sql + "\n)";

        System.out.println("[ sql4: " + sql + " ]");

        st = conn.prepareStatement(sql);

        int pos = 1;
        for (DataColumn d : t.getDataColumns()) {
          st.setString(pos++, d.getValue());
        }

        rs = st.executeQuery();
        System.out.println("[ insert executed ]");

        showKeys(rs);

        break;
      }

      case QUERY_OUTPUT_6: {

        List<String> generatedColumnNames = new ArrayList<String>();
        for (SequenceColumn c : t.getSequenceColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (IdentityColumn c : t.getIdentityColumns()) {
          generatedColumnNames.add(c.getName());
        }
        for (DefaultColumn c : t.getDefaultColumns()) {
          generatedColumnNames.add(c.getName());
        }

        // sql = "select " + ListWriter.render(generatedColumnNames, ", ") + "
        // from final table (\n " + sql + "\n)";

        sql = "insert into " + t.getTableName() + " (" + names.toString() + ") output "
            + ListWriter.render(generatedColumnNames, "inserted.", "", ", ") + " values (" + values.toString() + ")";

        System.out.println("[ sql4: " + sql + " ]");

        st = conn.prepareStatement(sql);

        int pos = 1;
        for (DataColumn d : t.getDataColumns()) {
          st.setString(pos++, d.getValue());
        }

        rs = st.executeQuery();
        System.out.println("[ insert executed ]");

        showKeys(rs);

        break;
      }

      default:

        System.out.println("[ No Retrieval type specified ]");

      }

    } finally {
      JdbcUtils.closeDbResources(st, rs);
      JdbcUtils.closeDbResources(cs);
    }
  }

  // Utilities

  private void showKeys(final ResultSet rs) throws SQLException {

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

  public static String getCoda(final List<String> columnNames) {
    ListWriter lw = new ListWriter(", ");
    for (String col : columnNames) {
      lw.add(col);
    }
    return " returning " + lw.toString();

  }

  // Classes

  public static class Table {

    private String tableName;
    private Column[] columns;

    public Table(final String tableName, final Column... columns) {
      this.tableName = tableName;
      this.columns = columns;
      int index = 1;
      for (Column c : this.columns) {
        c.setIndex(index++);
      }
    }

    public String getTableName() {
      return tableName;
    }

    public Column[] getColumns() {
      return columns;
    }

    public List<SequenceColumn> getSequenceColumns() {
      List<SequenceColumn> list = new ArrayList<SequenceColumn>();
      for (Column c : this.columns) {
        try {
          SequenceColumn s = (SequenceColumn) c;
          list.add(s);
        } catch (ClassCastException e) {
          // Ignore
        }
      }
      return list;
    }

    public List<IdentityColumn> getIdentityColumns() {
      List<IdentityColumn> list = new ArrayList<IdentityColumn>();
      for (Column c : this.columns) {
        try {
          IdentityColumn i = (IdentityColumn) c;
          list.add(i);
        } catch (ClassCastException e) {
          // Ignore
        }
      }
      return list;
    }

    public List<DefaultColumn> getDefaultColumns() {
      List<DefaultColumn> list = new ArrayList<DefaultColumn>();
      for (Column c : this.columns) {
        try {
          DefaultColumn i = (DefaultColumn) c;
          list.add(i);
        } catch (ClassCastException e) {
          // Ignore
        }
      }
      return list;
    }

    public List<DataColumn> getDataColumns() {
      List<DataColumn> list = new ArrayList<DataColumn>();
      for (Column c : this.columns) {
        try {
          DataColumn i = (DataColumn) c;
          list.add(i);
        } catch (ClassCastException e) {
          // Ignore
        }
      }
      return list;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder("Table " + this.tableName + "\n");
      for (SequenceColumn c : getSequenceColumns()) {
        sb.append(" - seq: " + c.toString() + "\n");
      }
      for (IdentityColumn c : getIdentityColumns()) {
        sb.append(" - ide: " + c.toString() + "\n");
      }
      for (DefaultColumn c : getDefaultColumns()) {
        sb.append(" - def: " + c.toString() + "\n");
      }
      for (DataColumn c : getDataColumns()) {
        sb.append(" - data: " + c.toString() + "\n");
      }
      return sb.toString();
    }

  }

  public static abstract class Column {

    private int index;
    private String name;

    public Column(final String name) {
      this.name = name;
    }

    void setIndex(int index) {
      this.index = index;
    }

    public int getIndex() {
      return index;
    }

    public String getName() {
      return name;
    }

    public String toString() {
      return "index:" + this.index + " name:" + this.name;
    }

  }

  public static class SequenceColumn extends Column {

    private String sequence;

    public SequenceColumn(final String name, final String sequence) {
      super(name);
      this.sequence = sequence;
    }

    public String getSequence() {
      return sequence;
    }

    public String toString() {
      return super.toString() + " sequence:" + this.sequence;
    }

  }

  public static class IdentityColumn extends Column {

    public IdentityColumn(final String name) {
      super(name);
    }

    public String toString() {
      return super.toString();
    }

  }

  public static class DefaultColumn extends Column {

    public DefaultColumn(String name) {
      super(name);
    }

    public String toString() {
      return super.toString();
    }

  }

  public static class DataColumn extends Column {

    private String value;

    public DataColumn(final String name, final String value) {
      super(name);
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public String toString() {
      return super.toString() + " value:" + this.value;
    }

  }

}
