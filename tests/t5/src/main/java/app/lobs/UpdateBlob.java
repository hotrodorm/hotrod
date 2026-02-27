package app.lobs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateBlob {

  public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

    System.out.println("=== Blob UPDATE - Starting ===");

    String url = args[0];
    String username = args[1];
    String password = args[2];

    System.out.println("* url: " + url);
    System.out.println("* username: " + username);
    System.out.println("* password: " + password);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    LocalDateTime dateTime = LocalDateTime.now();
    String ts = dateTime.format(formatter);

    // . Database---- setBlob()--- setBinaryStream()
    // . ------------ ------------ -----------------
    // . Oracle------ Yes--------- Yes
    // . DB2--------- Yes--------- Yes
    // . PostgreSQL-- No---------- Yes
    // . SQL Server-- Yes--------- Yes
    // . MySQL------- Yes--------- Yes
    // . MariaDB----- Yes--------- Yes

    try (Connection conn = DriverManager.getConnection(url, username, password);) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("update person set photo = ? where id = 1");) {
        String fn = "./data/car.png";
        try (InputStream is = new FileInputStream(fn);) {
//          ps.setBlob(1, is);
          ps.setBinaryStream(1, is);
          int count = ps.executeUpdate();
          System.out.println("Blob update (count=" + count + "): " + fn);
        }
      }
      conn.commit();
    }
    System.out.println("=== Blob UPDATE - Complete ===");

  }

}
