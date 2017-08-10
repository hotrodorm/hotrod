package examples.crud1;

import java.sql.Date;
import java.sql.SQLException;

import daos.DailyReportVO;
import daos.primitives.DailyReportDAO;

/**
 * Example CRUD 106 - Insert without Autogeneration
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud106 {

  public static void main(String[] args) throws SQLException {

    DailyReportVO r = new DailyReportVO();
    r.setBranchId(104); // PK column 1
    r.setReportDate(Date.valueOf("2017-02-28")); // PK column 2
    r.setTotalSold(524850L);
    int rows = DailyReportDAO.insert(r);
    System.out.println("Daily Report inserted (table with PK, but no auto-generation). Rows inserted=" + rows);

  }

}
