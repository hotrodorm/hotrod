package examples;

import java.sql.SQLException;

import daos.primitives.ClientDAO;
import daos.primitives.DailyReportDAO;

/**
 * Example 09 - Retrieving Sequence Values
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example09 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 09 - Retrieving Sequence Values ===");
    System.out.println(" ");

    // 1. Retrieve Sequence Value
    // Example: Retrieve a value from the sequence CLIENT_SEQ

    long value = ClientDAO.selectSequenceClientSeq();
    System.out.println("1. Retrieve Sequence Value - Retrieved CLIENT_SEQ value=" + value);

    // 2. Retrieve Sequence Value - Custom Method Name
    // Example: Retrieve a value from the sequence PDF_REPORT_FILE_SEQ

    long pdfSeq = DailyReportDAO.retrieveFileReportSequence();
    System.out.println("2. Retrieve Sequence Value - Custom Method Name - PDF_REPORT_FILE_SEQ value=" + pdfSeq);

    System.out.println(" ");
    System.out.println("=== Example 09 Complete ===");

  }

}
