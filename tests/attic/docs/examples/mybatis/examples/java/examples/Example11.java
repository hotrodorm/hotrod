package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.primitives.DailyOperations;

/**
 * Example 11 - Custom DAOs
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example11 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 11 - Custom DAOs ===");
    System.out.println(" ");

    // 1. Retrieve Sequence Value from Custom DAO DailyOperations
    // Example: Retrieve a value from the sequence DATA_FILE_SEQ

    long value = DailyOperations.selectSequenceDataFileSeq();
    System.out
        .println("1. Retrieve Sequence Value from Custom DAO DailyOperations - Retrieved DATA_FILE_SEQ value=" + value);

    // 2. Execute Regular Query
    // Example: Close the day summarizing all sales

    int rows = DailyOperations.closeDay(Date.valueOf("2017-02-28"));
    System.out.println("2. Execute Regular Query - Close the day summarizing all sales. Affected rows=" + rows);

    System.out.println(" ");
    System.out.println("=== Example 11 Complete ===");

  }

}
