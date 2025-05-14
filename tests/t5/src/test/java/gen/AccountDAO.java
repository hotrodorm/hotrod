package gen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.data.RowReader;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.PreparedModificationQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.dynamicsql.insert.PreparedInsertQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.springframework.beans.factory.annotation.Autowired;

import test.T5.Data;

public class AccountDAO {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AccountDAO.class.getName());

  @Autowired
  private DynamicSQL assembler;

  private final DynamicSelectQuery selectByExample = assembler //
      .literal("SELECT id, name, type, balance\n") //
      .literal("FROM account") //
      .where("AND") //
        .if_("f.id != null").literal("id = ").parameterNullable("f.id", Types.NUMERIC).endif()
        .if_("f.name != null").literal("name = ").parameterNullable("f.name", Types.VARCHAR).endif()
        .if_("f.type != null").literal("type = ").parameterNullable("f.type", Types.VARCHAR).endif()
        .if_("f.balance != null").literal("balance = ").parameterNullable("f.balance", Types.NUMERIC).endif()
      .endwhere()
      .endSelectQuery();

  public List<Account> select(DataSource dataSource, Account filter) throws DynamicExpressionException, SQLException {

    try (Connection conn = dataSource.getConnection()) {

      // 1. Prepare the parameter context

      Parameters context = this.assembler.newParameters();
      context.add("f", filter);

      // 2. Execute the resulting query

      RowReader<Account> rowReader = new RowReader<Account>() {

        @Override
        public Account readRowFrom(ResultSet rs, Connection conn) throws SQLException {
          Account row = new Account();
          row.setId(rs.getInt(1));
          row.setName(rs.getString(2));
          row.setType(rs.getString(3));
          row.setBalance(rs.getInt(4));
          return row;
        }

      };

      // 3. Process DynamicSQL and produce query and parameters

      PreparedSelectQuery<Account> preparedQuery = this.selectByExample.prepare(context, rowReader);
      System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

      List<Account> accounts = preparedQuery.execute(conn);

      return accounts;

    }

  }

  // ====================
  // === NO RETRIEVAL ===
  // ====================

//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (") //
//      .ifPart("n.id != null", builder.literal("id, ").end()).literal("name, type, balance)\n") //
//      .literal("VALUES (") //
//      .ifPart("n.id != null", builder.parameter("n.id", Types.NUMERIC).literal(", ").end())
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.NO_RETRIEVAL);

  // ================
  // === IDENTITY ===
  // ================

//  // Oracle - Identity Inline
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (") //
//      .literal("name, type, balance)\n") //
//      .literal("VALUES (") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET, null, null, "id");

//  // DB2, PostgreSQL, SQL Server, MySQL, MariaDB, Sybase ASE, H2, HyperSQL, and Derby - Identity Inline
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (") //
//      .ifPart("n.id != null", builder.literal("id, ").end()).literal("name, type, balance)\n") //
//      .literal("VALUES (") //
//      .ifPart("n.id != null", builder.parameter("n.id", Types.NUMERIC).literal(", ").end())
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET);

  // =======================
  // === SEQUENCE INLINE ===
  // =======================

//  // Oracle - Sequence Inline
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .literal("seq_account.NEXTVAL, ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, null, null, "id");

//  // DB2 - Sequence Inline
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .literal("NEXT VALUE FOR seq_account") //
//      .literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, null, null, "id");

//  // PostgreSQL - Sequence Inline
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .literal("NEXTVAL('seq_account')") //
//      .literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET);

//  // SQL Server - Sequence Inline
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") OUTPUT INSERTED.id VALUES (\n  ") //
//      .literal("NEXT VALUE FOR seq_account") //
//      .literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_STANDARD_RESULTSET);

  // H2, HyperSQL, and Derby - Sequence Inline
  private final DynamicInsertQuery insert = assembler //
      .literal("INSERT INTO account (") //
      .literal("  id, name, type, balance\n") //
      .literal(") VALUES (\n  ") //
      .literal("NEXT VALUE FOR seq_account") //
      .literal(", ") //
      .parameterNullable("n.name", Types.VARCHAR) //
      .literal(", ") //
      .parameterNullable("n.type", Types.VARCHAR) //
      .literal(", ") //
      .parameterNullable("n.balance", Types.NUMERIC) //
      .literal(")") //
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET);

  // =========================
  // === SEQUENCE PREFETCH ===
  // =========================

//  // Oracle - Sequence Prefetch
//  private static final InsertProperties ORACLE_SEQUENCE_INSERT_PROPERTIES = new InsertProperties(
//      "SELECT seq_account.NEXTVAL FROM DUAL", "n.id");
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .parameter("n.id", Types.NUMERIC).literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, ORACLE_SEQUENCE_INSERT_PROPERTIES);

//  // DB2 - Sequence Prefetch
//  private static final InsertProperties DB2_SEQUENCE_INSERT_PROPERTIES = new InsertProperties(
//      "VALUES NEXT VALUE FOR seq_account", "n.id");
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .parameter("n.id", Types.NUMERIC).literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, DB2_SEQUENCE_INSERT_PROPERTIES);

//  // PostgreSQL - Sequence Prefetch
//  private static final InsertProperties POSTGRESQL_SEQUENCE_INSERT_PROPERTIES = new InsertProperties(
//      "SELECT NEXTVAL('seq_account')", "n.id");
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .parameter("n.id", Types.NUMERIC).literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, POSTGRESQL_SEQUENCE_INSERT_PROPERTIES);

//  // SQL Server - Sequence Prefetch
//  private static final InsertProperties SQLSERVER_SEQUENCE_INSERT_PROPERTIES = new InsertProperties(
//      "SELECT NEXT VALUE FOR seq_account", "n.id");
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .parameter("n.id", Types.NUMERIC).literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, SQLSERVER_SEQUENCE_INSERT_PROPERTIES);

//  // H2 sequence Prefetch
//  private static final InsertProperties H2_IDENTITY_INSERT_PROPERTIES = new InsertProperties(
//      "SELECT NEXT VALUE FOR seq_account", "n.id");
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .parameter("n.id", Types.NUMERIC).literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, H2_IDENTITY_INSERT_PROPERTIES);

//  // HyperSQL, and Derby sequence Prefetch
//  private static final InsertProperties H2_IDENTITY_INSERT_PROPERTIES = new InsertProperties(
//      "VALUES NEXT VALUE FOR seq_account", "n.id");
//  private final DynamicInsertQuery insert = builder //
//      .literal("INSERT INTO account (\n") //
//      .literal("  id, name, type, balance\n") //
//      .literal(") VALUES (\n  ") //
//      .parameter("n.id", Types.NUMERIC).literal(", ") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, H2_IDENTITY_INSERT_PROPERTIES);

  public void insert(Connection conn, Account entity) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    Parameters context = this.assembler.newParameters();
    context.add("n", entity);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedInsertQuery preparedQuery = this.insert.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    Long id = preparedQuery.execute(conn);
    entity.setId(id == null ? null : id.intValue());

  }

  private final DynamicModificationQuery updateByExample = assembler //
      .literal("UPDATE account") //
      .set() //
          .if_("n.id != null").literal("id = ").parameterNullable("n.id", Types.NUMERIC).endif()
          .if_("n.name != null").literal("name = ").parameterNullable("n.name", Types.VARCHAR).endif()
          .if_("n.type != null").literal("type = ").parameterNullable("n.type", Types.VARCHAR).endif()
          .if_("n.balance != null").literal("balance = ").parameterNullable("n.balance", Types.NUMERIC).endif()
      .endset() //
      .where("AND") //
          .if_("f.id != null").literal("id = ").parameterNullable("f.id", Types.NUMERIC).endif()
          .if_("f.name != null").literal("name = ").parameterNullable("f.name", Types.VARCHAR).endif()
          .if_("f.type != null").literal("type = ").parameterNullable("f.type", Types.VARCHAR).endif()
          .if_("f.balance != null").literal("balance = ").parameterNullable("f.balance", Types.NUMERIC).endif()
      .endwhere() //
      .endModificationQuery();

  public int update(Connection conn, Account filter, Account newValues)
      throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    Parameters context = this.assembler.newParameters();
    context.add("f", filter);
    context.add("n", newValues);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedModificationQuery preparedQuery = this.updateByExample.prepare(context);
    log.info("Preview:\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

  @SuppressWarnings("unused")
  private final DynamicModificationQuery deleteByPK = assembler //
      .literal("DELETE FROM account\n") //
      .literal("WHERE id = ").parameterNullable("f.id", Types.NUMERIC).literal("  AND id2 = ")
      .parameterNullable("f.id2", Types.NUMERIC).endModificationQuery();

  private final DynamicModificationQuery deleteByExample = assembler //
      .literal("DELETE FROM account") //
      .where("AND") //
          .if_("f.id != null").literal("id = ").parameterNullable("f.id", Types.NUMERIC).endif()
          .if_("f.name != null").literal("name = ").parameterNullable("f.name", Types.VARCHAR).endif()
          .if_("f.type != null").literal("type = ").parameterNullable("f.type", Types.VARCHAR).endif()
          .if_("f.balance != null").literal("balance = ").parameterNullable("f.balance", Types.NUMERIC).endif()
      .endwhere()
      .endModificationQuery();

  @SuppressWarnings("unused")
  private DataSource dataSource;

  public int delete(Connection conn, Account filter) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    Parameters context = this.assembler.newParameters();
    context.add("f", filter);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedModificationQuery preparedQuery = this.deleteByExample.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

  // === OTHER TESTS ===

  public void testChoose(Account filter) throws DynamicExpressionException {

    DynamicModificationQuery d1 = assembler //
        .literal("DELETE FROM account\nWHERE ") //
        .choose() //
            .when("f.id != null").literal("id = ").parameterNullable("f.id", Types.NUMERIC).endwhen()
            .when("f.name != null").literal("name = ").parameterNullable("f.name", Types.VARCHAR).endwhen()
            .when("f.type != null").literal("type = ").parameterNullable("f.type", Types.VARCHAR).endwhen()
            .otherwise().literal("1 = 1").endotherwise() 
        .endchoose()
        .endModificationQuery();

    Parameters context = this.assembler.newParameters();
    context.add("f", filter);

    PreparedModificationQuery preparedQuery = d1.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

  }

  public void testForeach(Data data) throws DynamicExpressionException {

    DynamicModificationQuery d1 = assembler //
        .literal("DELETE FROM account\nWHERE code in ") //
        .foreach("t", "d.tags", "(", ", ", ")")
            .parameterNullable("t", Types.VARCHAR)
            .foreach("c", "d.codes", "(", ", ", ")").parameterNullable("c", Types.NUMERIC).endforeach()
        .endforeach()
        .endModificationQuery();

    Parameters context = this.assembler.newParameters();
    context.add("d", data);

    PreparedModificationQuery preparedQuery = d1.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

  }

  public void testBind(DataSource dataSource, Data data) throws DynamicExpressionException, SQLException {

    try (Connection conn = dataSource.getConnection()) {

      DynamicSelectQuery d1 = assembler //
          .literal("SELECT COUNT(*) FROM account\n") //
          .bind("pattern", "'%' + d.name + '%'") //
          .literal("WHERE type LIKE ") //
          .parameterNullable("pattern", Types.VARCHAR) //
          .endSelectQuery();

      Parameters context = this.assembler.newParameters();
      context.add("d", data);

      RowReader<Long> countReader = new RowReader<Long>() {

        @Override
        public Long readRowFrom(ResultSet rs, Connection conn) throws SQLException {
          return rs.getLong(1);
        }

      };

      PreparedSelectQuery<Long> preparedQuery = d1.prepare(context, countReader);
      System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

      List<Long> counts = preparedQuery.execute(conn);
      System.out.println("rows: " + counts.get(0));

    }

  }

  public void testTrim(Account filter) throws DynamicExpressionException {

    DynamicModificationQuery d1 = assembler //
        .literal("DELETE FROM account") //
        .trim("H", "S", "T", "\nh<", ">h", "\ns<", ">s", "\nt<", ">t")
            .if_("true").literal("A").endif()
            .if_("true").literal("B").endif()
            .if_("true").literal("C").endif()
        .endtrim()
        .endModificationQuery();

    Parameters context = this.assembler.newParameters();
    context.add("f", filter);

    PreparedModificationQuery preparedQuery = d1.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

  }

}
