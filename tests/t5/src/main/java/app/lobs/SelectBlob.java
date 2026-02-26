package app.lobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReadBlob {

  public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

    System.out.println("=== Blob Reader ===");

    String url = args[0];
    String username = args[1];
    String password = args[2];

    System.out.println("* url: " + url);
    System.out.println("* username: " + username);
    System.out.println("* password: " + password);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    LocalDateTime dateTime = LocalDateTime.now();
    String ts = dateTime.format(formatter);

    try (Connection conn = DriverManager.getConnection(url, username, password);) {
      try (PreparedStatement ps = conn.prepareStatement("select photo from person");) {
        try (ResultSet rs = ps.executeQuery();) {
          int rn = 1;
          while (rs.next()) {
            System.out.println("> reading row #" + rn);
            String fn = "./output/" + ts + "-" + rn + ".png";
            try (InputStream is = rs.getBinaryStream(1);) {
              try (OutputStream os = new FileOutputStream(new File(fn));) {
                long bytes = copy(is, os);
                System.out.println("File (" + bytes + " bytes): " + fn);
                rn++;
              }
            }
          }
        }
      }
    }
    System.out.println("=== Blob Reader - Complete ===");

  }

  private static long copy(InputStream source, OutputStream target) throws IOException {
    long bytes = 0;
    byte[] buf = new byte[8192];
    int length;
    while ((length = source.read(buf)) != -1) {
      target.write(buf, 0, length);
//      System.out.println("> " + HexaUtils.toHexa(buf, 0, length));
      bytes = bytes + length;
    }
    return bytes;
  }

}
