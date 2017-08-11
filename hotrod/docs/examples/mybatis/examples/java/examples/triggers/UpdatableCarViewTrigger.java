package examples.triggers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.TriggerAdapter;

public class UpdatableCarViewTrigger extends TriggerAdapter {

  private PreparedStatement delete;
  private PreparedStatement insert;
  private PreparedStatement update;

  @Override
  public void init(final Connection conn, final String schemaName, final String triggerName, final String tableName,
      final boolean before, final int type) throws SQLException {
    log("schemaName=" + schemaName + " triggerName=" + triggerName + " tableName=" + tableName + " before=" + before
        + " type=" + type);
    this.delete = conn.prepareStatement("delete from vehicle where id = ?");
    this.insert = conn.prepareStatement("insert into vehicle values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    this.update = conn.prepareStatement("update vehicle set "
        + "brand = ?, model = ?, type = ?, vin = ?, engine_number = ?, mileage = ?, purchased_on = ?, branch_id = ?, list_price = ?, sold = ? "
        + "where id = ?");
    super.init(conn, schemaName, triggerName, tableName, before, type);
  }

  @Override
  public void fire(final Connection conn, final ResultSet oldRow, final ResultSet newRow) throws SQLException {

    try {

      // DELETE

      if (oldRow != null && newRow == null && oldRow.next()) {
        int id = oldRow.getInt(1);
        this.delete.setInt(1, id);
        this.delete.execute();
        log("Vehicle #" + id + " deleted");
      }

      // UPDATE

      if (oldRow != null && newRow != null && oldRow.next() && newRow.next()) {
        int rcol = 1;

        int id = newRow.getInt(rcol++);
        String brand = newRow.getString(rcol++);
        String model = newRow.getString(rcol++);
        String type = newRow.getString(rcol++);
        String vin = newRow.getString(rcol++);
        String engineNumber = newRow.getString(rcol++);

        Integer mileage = newRow.getInt(rcol++);
        if (newRow.wasNull()) {
          mileage = null;
        }

        Date purchasedOn = newRow.getDate(rcol++);

        Integer branchId = newRow.getInt(rcol++);
        if (newRow.wasNull()) {
          branchId = null;
        }

        Integer listPrice = newRow.getInt(rcol++);
        if (newRow.wasNull()) {
          listPrice = null;
        }

        Boolean sold = newRow.getBoolean(rcol++);

        int ucol = 1;
        this.update.setString(ucol++, brand);
        this.update.setString(ucol++, model);
        this.update.setString(ucol++, type);
        this.update.setString(ucol++, vin);
        this.update.setString(ucol++, engineNumber);

        if (mileage != null) {
          this.update.setInt(ucol++, mileage);
        } else {
          this.update.setNull(ucol++, Types.NUMERIC);
        }

        this.update.setDate(ucol++, purchasedOn);

        if (branchId != null) {
          this.update.setInt(ucol++, branchId);
        } else {
          this.update.setNull(ucol++, Types.NUMERIC);
        }

        if (listPrice != null) {
          this.update.setInt(ucol++, listPrice);
        } else {
          this.update.setNull(ucol++, Types.NUMERIC);
        }

        this.update.setBoolean(ucol++, sold);

        this.update.setInt(ucol++, id);
        this.update.execute();
        log("Vehicle #" + id + " updated");
      }

      // INSERT

      if (oldRow == null && newRow != null && newRow.next()) {
        int col = 1;
        int id = newRow.getInt(col);
        this.insert.setInt(col++, id);
        this.insert.setString(col, newRow.getString(col++));
        this.insert.setString(col, newRow.getString(col++));
        this.insert.setString(col, newRow.getString(col++));
        this.insert.setString(col, newRow.getString(col++));
        this.insert.setString(col, newRow.getString(col++));
        this.insert.setInt(col, newRow.getInt(col++));
        this.insert.setDate(col, newRow.getDate(col++));
        this.insert.setInt(col, newRow.getInt(col++));
        this.insert.setInt(col, newRow.getInt(col++));
        this.insert.setBoolean(col, newRow.getBoolean(col++));
        this.insert.execute();
        log("Vehicle inserted");
      }

    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }

  }

  private void log(final String txt) {
    System.out.println("[UpdatableCarViewTrigger] - " + txt);
  }

}