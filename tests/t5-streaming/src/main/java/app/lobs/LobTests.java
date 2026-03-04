package app.lobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LobTests {

  private static String url;
  private static String username;
  private static String password;

  private static final Verifier VERIFIER = new Verifier();

  public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

    System.out.println("=== LOB Tester - Starting ===");

    url = args[0];
    username = args[1];
    password = args[2];

    System.out.println("Connected to: " + url);
    System.out.println("Username: " + username);

    String tid = getTestId();

    selectSingleRow(tid);
    selectMultiRow(tid);
//    selectByLobs(tid);
    selectByLobsOracle(tid);
    insert(tid);
    updateByPK(tid);
//    updateByLobs(tid);
    updateByLobsOracle(tid);
//    deleteByLobs(tid);
    deleteByLobsOracle(tid);

    /**
     * <pre>
    . Database   | select1 | selectN | selectByLobs | insert | updateByPK | updateByLobs | deleteByLobs
    . ---------- | ------- | ------- | ------------ | ------ | ---------- | ------------ | ------------
    . Oracle     | Yes     | Yes     | ?            | Yes    | Yes        | ?            | ?
    . DB2        | Yes     | Yes     | Yes          | Yes    | Yes        | Yes          | Yes
    . PostgreSQL | Yes     | Yes     | Yes          | Yes    | Yes        | Yes          | Yes
    . SQL Server | Yes     | Yes     | Yes          | Yes    | Yes        | Yes          | Yes
    . MySQL      | Yes     | Yes     | Yes          | Yes    | Yes        | Yes          | Yes
    . MariaDB    | Yes     | Yes     | Yes          | Yes    | Yes        | Yes          | Yes
    . H2         | Yes     | Yes     | Yes          | Yes    | Yes        | ?            | ?
    .            |         |         |              |        |            |              |
    
     . Database---- setBlob()--- setBinaryStream()
     . ------------ ------------ -----------------
     . Oracle------ Yes--------- Yes
     . DB2--------- Yes--------- Yes
     . PostgreSQL-- No---------- Yes
     . SQL Server-- Yes--------- Yes
     . MySQL------- Yes--------- Yes
     . MariaDB----- Yes--------- Yes
     * 
     * </pre>
     */
    System.out.println("* " + VERIFIER);

    System.out.println("=== LOB Tester - Complete ===");

  }

  private static final byte[] B1 = HexaUtils.toByteArray("00112233");
  private static final byte[] B2 = HexaUtils.toByteArray("12345678");
  private static final byte[] B3 = HexaUtils.toByteArray("89504e47");
  private static final byte[] B4 = HexaUtils.toByteArray("00112233");
  private static final byte[] B5 = HexaUtils.toByteArray("aabbccdd");
  private static final byte[] B6 = HexaUtils.toByteArray("66778899");

  private static final String C1 = "This is a job contract";
  private static final String C2 = "This is a job contract for Archie";
  private static final String C3 = "This is a job contract for Anne";
  private static final String C4 = "This is a job contract";
  private static final String C5 = "This is a job contract 5.";
  private static final String C6 = "This is a job contract 6.";

//  (1, E'00112233'::bytea, 'This is a job contract', 'Alice'),
//  (2, E'12345678'::bytea, 'This is a job contract for Archie', 'Archie'),
//  (3, E'89504e47'::bytea, 'This is a job contract for Anne', 'Anne'),
//  (4, E'00112233'::bytea, 'This is a job contract', 'Alan');

//rows:
//-- 1: b1, c1
//-- 2: b2, c2
//-- 3: b3, c3
//-- 4: b2, c2
//1. SELECT blob+clob, single row #2  
//2. SELECT blob+clob, multi-row (#2, #4)
//3. SELECT blob+clob, multi-row, with blob+clob search criteria (#1, #4)
//4. INSERT with blob+clob (#5, b5, c5)
//5. UPDATE blob+clob by PK (#3, b5, c5)
//6. UPDATE blob+clob with blob+clob search criteria (#3, b6, c6 from b5, c5)
//7. DELETE with blob+clob search criteria (#5, b4, c4)

  private static void selectSingleRow(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n1. Select Single Row.");
    try (Connection conn = getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement("SELECT id, photo, contract FROM person WHERE id = 1");) {
        try (ResultSet rs = ps.executeQuery()) {
          int rn = 0;
          while (rs.next()) {
            rn++;
            int id = rs.getInt(1);
            VERIFIER.equals(rs, 2, B1);
            VERIFIER.equals(rs, 3, C1);
          }
          VERIFIER.equals(rn, 1);
        }
      }
    }
  }

  private static void selectMultiRow(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n2. Select Multi Row.");
    try (Connection conn = getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement("SELECT id, photo, contract FROM person WHERE id in (2, 4)");) {
        try (ResultSet rs = ps.executeQuery()) {
          int rn = 0;
          while (rs.next()) {
            rn++;
            int id = rs.getInt(1);
            VERIFIER.equals(rs, 2, id == 2 ? B2 : B4);
            VERIFIER.equals(rs, 3, id == 2 ? C2 : C4);
          }
          VERIFIER.equals(rn, 2);
        }
      }
    }
  }

  private static void selectByLobs(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n3. SELECT blob+clob, multi-row, with blob+clob search criteria.");
    try (InputStream isPhoto = new FileInputStream("data/b1.png")) {
      try (Reader readerContract = new FileReader("data/c1.txt")) {
        try (Connection conn = getConnection()) {
          try (PreparedStatement ps = conn
              .prepareStatement("SELECT id, photo, contract FROM person WHERE photo = ? AND contract = ?");) {
            ps.setBinaryStream(1, isPhoto);
            ps.setCharacterStream(2, readerContract);
            try (ResultSet rs = ps.executeQuery()) {
              int rn = 0;
              while (rs.next()) {
                rn++;
                int id = rs.getInt(1);
                VERIFIER.equals(rs, 2, id == 1 ? B1 : B4);
                VERIFIER.equals(rs, 3, id == 1 ? C1 : C4);
              }
              VERIFIER.equals(rn, 2);
            }
          }
        }
      }
    }
  }

  private static void selectByLobsOracle(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n3. SELECT blob+clob, multi-row, with blob+clob search criteria.");
    try (InputStream isPhoto = new FileInputStream("data/b1.png")) {
      try (Reader readerContract = new FileReader("data/c1.txt")) {
        try (Connection conn = getConnection()) {
          try (PreparedStatement ps = conn.prepareStatement(
              "SELECT id, photo, contract FROM person WHERE dbms_lob.compare(photo, ?) = 0 AND dbms_lob.compare(contract, ?) = 0");) {
            setBlob(ps, 1, isPhoto, conn);
            ps.setCharacterStream(2, readerContract);
            try (ResultSet rs = ps.executeQuery()) {
              int rn = 0;
              while (rs.next()) {
                rn++;
                int id = rs.getInt(1);
                VERIFIER.equals(rs, 2, id == 1 ? B1 : B4);
                VERIFIER.equals(rs, 3, id == 1 ? C1 : C4);
              }
              VERIFIER.equals(rn, 2);
            }
          }
        }
      }
    }
  }

  private static void setBlob(PreparedStatement ps, int ordinal, InputStream isPhoto, Connection conn)
      throws SQLException, IOException {
    Blob blob = conn.createBlob();
    ps.setBlob(ordinal, blob);
    try (OutputStream os = blob.setBinaryStream(1)) {
      byte[] buffer = new byte[10 * 1024];
      int nread = 0;
      int bytes = 0;
      while ((nread = isPhoto.read(buffer)) != -1) {
        System.out.println("* read: " + HexaUtils.toHexa(buffer, 0, nread));
        os.write(buffer, 0, nread);
        bytes = bytes + nread;
      }
      System.out.println("* bytes=" + bytes);
    }
  }

  private static void insert(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n4. INSERT.");
    try (InputStream isPhoto = new FileInputStream("data/b5.png")) {
      try (Reader readerContract = new FileReader("data/c5.txt")) {
        try (Connection conn = getConnection()) {
          try (PreparedStatement ps = conn
              .prepareStatement("INSERT INTO person (id, photo, contract) VALUES (?, ?, ?)");) {
            ps.setInt(1, 5);
            ps.setBinaryStream(2, isPhoto);
            ps.setCharacterStream(3, readerContract);
            int count = ps.executeUpdate();
            VERIFIER.equals(count, 1);
          }
        }
      }
    }
  }

  private static void updateByPK(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n5. UPDATE by PK.");
    try (InputStream isPhoto = new FileInputStream("data/b5.png")) {
      try (Reader readerContract = new FileReader("data/c5.txt")) {
        try (Connection conn = getConnection()) {
          try (PreparedStatement ps = conn.prepareStatement( //
              "UPDATE person SET photo = ?, contract = ? WHERE id = 3");) {
            ps.setBinaryStream(1, isPhoto);
            ps.setCharacterStream(2, readerContract);
            int count = ps.executeUpdate();
            VERIFIER.equals(count, 1);
          }
        }
      }
    }
  }

  private static void updateByLobs(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n6. UPDATE by LOBs.");
    try (InputStream streamVPhoto = new FileInputStream("data/b6.png")) {
      try (Reader readerVContract = new FileReader("data/c6.txt")) {
        try (InputStream streamSPhoto = new FileInputStream("data/b5.png")) {
          try (Reader readerSContract = new FileReader("data/c5.txt")) {
            try (Connection conn = getConnection()) {
              try (PreparedStatement ps = conn
                  .prepareStatement("UPDATE person SET photo = ?, contract = ? WHERE photo = ? AND contract = ?");) {
                ps.setBinaryStream(1, streamVPhoto);
                ps.setCharacterStream(2, readerVContract);
                ps.setBinaryStream(3, streamSPhoto);
                ps.setCharacterStream(4, readerSContract);
                int count = ps.executeUpdate();
                VERIFIER.equals(count, 2);
              }
            }
          }
        }
      }
    }
  }

  private static void updateByLobsOracle(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n6. UPDATE by LOBs.");
    try (InputStream streamVPhoto = new FileInputStream("data/b6.png")) {
      try (Reader readerVContract = new FileReader("data/c6.txt")) {
        try (InputStream streamSPhoto = new FileInputStream("data/b5.png")) {
          try (Reader readerSContract = new FileReader("data/c5.txt")) {
            try (Connection conn = getConnection()) {
              try (PreparedStatement ps = conn.prepareStatement(
                  "UPDATE person SET photo = ?, contract = ? WHERE dbms_lob.compare(photo, ?) = 0 AND dbms_lob.compare(contract, ?) = 0");) {
                setBlob(ps, 1, streamVPhoto, conn);
                ps.setCharacterStream(2, readerVContract);
                setBlob(ps, 3, streamSPhoto, conn);
                ps.setCharacterStream(4, readerSContract);
                int count = ps.executeUpdate();
                VERIFIER.equals(count, 2);
              }
            }
          }
        }
      }
    }
  }

  private static void deleteByLobs(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n7. DELETE.");
    try (InputStream streamSPhoto = new FileInputStream("data/b6.png")) {
      try (Reader readerSContract = new FileReader("data/c6.txt")) {
        try (Connection conn = getConnection()) {
          try (PreparedStatement ps = conn.prepareStatement("DELETE FROM person WHERE photo = ? AND contract = ?");) {
            ps.setBinaryStream(1, streamSPhoto);
            ps.setCharacterStream(2, readerSContract);
            int count = ps.executeUpdate();
            VERIFIER.equals(count, 2);
          }
        }
      }
    }
  }

  private static void deleteByLobsOracle(String tid) throws SQLException, IOException, FileNotFoundException {
    System.out.println("\n7. DELETE.");
    try (InputStream streamSPhoto = new FileInputStream("data/b6.png")) {
      try (Reader readerSContract = new FileReader("data/c6.txt")) {
        try (Connection conn = getConnection()) {
          try (PreparedStatement ps = conn.prepareStatement(
              "DELETE FROM person WHERE dbms_lob.compare(photo, ?) = 0 AND dbms_lob.compare(contract, ?) = 0");) {
            setBlob(ps, 1, streamSPhoto, conn);
            ps.setCharacterStream(2, readerSContract);
            int count = ps.executeUpdate();
            VERIFIER.equals(count, 2);
          }
        }
      }
    }
  }

  // Utils

  private static String getTestId() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    String ts = LocalDateTime.now().format(formatter);
    return ts;
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }

//  private static byte[] copy(InputStream source, OutputStream target) throws IOException {
//    long bytes = 0;
//    byte[] buf = new byte[8192];
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    int length;
//    while ((length = source.read(buf)) != -1) {
//      target.write(buf, 0, length);
//      baos.write(buf, 0, length);
//      bytes = bytes + length;
//    }
//    return baos.toByteArray();
//  }

}
