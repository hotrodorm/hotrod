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
  public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type)
      throws SQLException {
    this.prepDelete = conn.prepareStatement("delete from car where id = ?");
    this.prepInsert = conn.prepareStatement("insert into car values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    super.init(conn, schemaName, triggerName, tableName, before, type);
  }

  @Override
  public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
    if (oldRow != null && oldRow.next()) {
      int id = oldRow.getInt(1);
      this.prepDelete = conn.prepareStatement("delete from car where id = " + id);
      // this.prepDelete.setInt(1, id);
      this.prepDelete.execute();
    }
    if (newRow != null && newRow.next()) {
      int col = 1;
      this.prepInsert.setInt(col, newRow.getInt(col++));
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
  }

}