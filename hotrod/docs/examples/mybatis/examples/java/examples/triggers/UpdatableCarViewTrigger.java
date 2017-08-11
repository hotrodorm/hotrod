package examples.triggers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;

public class UpdatableCarViewTrigger extends TriggerAdapter {

  private PreparedStatement prepDelete;
  private PreparedStatement prepInsert;

  @Override
  public void init(final Connection conn, final String schemaName, final String triggerName, final String tableName,
      final boolean before, final int type) throws SQLException {
    log("schemaName=" + schemaName + " triggerName=" + triggerName + " tableName=" + tableName + " before=" + before
        + " type=" + type);
    this.prepDelete = conn.prepareStatement("delete from vehicle where id = ?");
    this.prepInsert = conn.prepareStatement("insert into vehicle values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    super.init(conn, schemaName, triggerName, tableName, before, type);
  }

  @Override
  public void fire(final Connection conn, final ResultSet oldRow, final ResultSet newRow) throws SQLException {
    try {
      log("oldRow=" + oldRow + " newRow=" + newRow);
      if (oldRow != null && oldRow.next()) {
        int id = oldRow.getInt(1);
        log("DELETE id=" + id);
        this.prepDelete.setInt(1, id);
        this.prepDelete.execute();
      }
      if (newRow != null && newRow.next()) {
        int col = 1;

        int id = newRow.getInt(col);
        this.prepInsert.setInt(col++, id);
        log("INSERT id=" + id);

        this.prepInsert.setString(col, newRow.getString(col++));
        this.prepInsert.setString(col, newRow.getString(col++));
        this.prepInsert.setString(col, newRow.getString(col++));
        this.prepInsert.setString(col, newRow.getString(col++));
        this.prepInsert.setString(col, newRow.getString(col++));
        this.prepInsert.setInt(col, newRow.getInt(col++));
        this.prepInsert.setDate(col, newRow.getDate(col++));
        this.prepInsert.setInt(col, newRow.getInt(col++));
        this.prepInsert.setInt(col, newRow.getInt(col++));
        this.prepInsert.setBoolean(col, newRow.getBoolean(col++));
        this.prepInsert.execute();
      }
    } catch (RuntimeException e) {
      e.printStackTrace(System.out);
      throw e;
    }
  }

  private void log(final String txt) {
    System.out.println("[TRG] - " + txt);
  }

}