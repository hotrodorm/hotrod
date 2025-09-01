package examples.crud1;

import java.sql.SQLException;
import java.sql.Timestamp;

import daos.VisitVO;
import daos.primitives.VisitDAO;

/**
 * Example CRUD 107 - Insert with No PK
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud107 {

  public static void main(String[] args) throws SQLException {

    VisitVO vi = new VisitVO();
    vi.setRecordedAt(Timestamp.valueOf("2017-03-01 11:30:00"));
    vi.setBranchId(105);
    vi.setNotes("This is a note of the visit");
    int rows = VisitDAO.insert(vi);
    System.out.println("Visit inserted (table with no PK). Rows inserted=" + rows);

  }

}
