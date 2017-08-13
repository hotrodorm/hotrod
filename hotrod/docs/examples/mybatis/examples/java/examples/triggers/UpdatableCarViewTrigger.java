package examples.triggers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    this.insert = conn.prepareStatement(
        "insert into vehicle (brand, model, type, vin, engine_number, mileage, purchased_on, branch_id, list_price, sold) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
        Integer id = JdbcUtil.getIntObj(newRow, rcol++);
        String brand = JdbcUtil.getString(newRow, rcol++);
        String model = JdbcUtil.getString(newRow, rcol++);
        String vin = JdbcUtil.getString(newRow, rcol++);
        String engineNumber = JdbcUtil.getString(newRow, rcol++);
        Integer mileage = JdbcUtil.getIntObj(newRow, rcol++);
        Date purchasedOn = JdbcUtil.getSQLDate(newRow, rcol++);
        Integer branchId = JdbcUtil.getIntObj(newRow, rcol++);
        Integer listPrice = JdbcUtil.getIntObj(newRow, rcol++);
        Boolean sold = JdbcUtil.getBooleanObj(newRow, rcol++);

        int icol = 1;
        JdbcUtil.setString(this.update, icol++, brand);
        JdbcUtil.setString(this.update, icol++, model);
        JdbcUtil.setString(this.update, icol++, "CAR");
        JdbcUtil.setString(this.update, icol++, vin);
        JdbcUtil.setString(this.update, icol++, engineNumber);
        JdbcUtil.setInt(this.update, icol++, mileage);
        JdbcUtil.setSQLDate(this.update, icol++, purchasedOn);
        JdbcUtil.setInt(this.update, icol++, branchId);
        JdbcUtil.setInt(this.update, icol++, listPrice);
        JdbcUtil.setBoolean(this.update, icol++, sold);
        JdbcUtil.setInt(this.update, icol++, id);
        this.update.execute();

        log("Vehicle #" + id + " updated");

      }

      // INSERT

      if (oldRow == null && newRow != null && newRow.next()) {

        int rcol = 1;
        Integer id = JdbcUtil.getIntObj(newRow, rcol++);
        String brand = JdbcUtil.getString(newRow, rcol++);
        String model = JdbcUtil.getString(newRow, rcol++);
        String vin = JdbcUtil.getString(newRow, rcol++);
        String engineNumber = JdbcUtil.getString(newRow, rcol++);
        Integer mileage = JdbcUtil.getIntObj(newRow, rcol++);
        Date purchasedOn = JdbcUtil.getSQLDate(newRow, rcol++);
        Integer branchId = JdbcUtil.getIntObj(newRow, rcol++);
        Integer listPrice = JdbcUtil.getIntObj(newRow, rcol++);
        Boolean sold = JdbcUtil.getBooleanObj(newRow, rcol++);

        int icol = 1;
        JdbcUtil.setString(this.insert, icol++, brand);
        JdbcUtil.setString(this.insert, icol++, model);
        JdbcUtil.setString(this.insert, icol++, "CAR");
        JdbcUtil.setString(this.insert, icol++, vin);
        JdbcUtil.setString(this.insert, icol++, engineNumber);
        JdbcUtil.setInt(this.insert, icol++, mileage);
        JdbcUtil.setSQLDate(this.insert, icol++, purchasedOn);
        JdbcUtil.setInt(this.insert, icol++, branchId);
        JdbcUtil.setInt(this.insert, icol++, listPrice);
        JdbcUtil.setBoolean(this.insert, icol++, sold);
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