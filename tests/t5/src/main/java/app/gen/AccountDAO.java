package app.gen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicInsertQuery;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.DynamicSelectQuery;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedModificationQuery;
import org.hotrod.dynamic.PreparedSelectQuery;
import org.hotrod.dynamic.PreparedSelectQuery.RowReader;
import org.hotrod.dynamic.builder.QueryBuilder;
import org.hotrod.dynamic.insert.InsertProperties;
import org.hotrod.dynamic.insert.PreparedInsertQuery;
import org.hotrod.dynamic.insert.PrimaryKeyRetrievalMode;

public class AccountDAO {

  private static final Logger log = Logger.getLogger(AccountDAO.class.getName());

  private final DynamicExpressionFactory factory = DynamicExpressionFactory.getFactory();
  private final QueryBuilder builder = new QueryBuilder(this.factory);

  private final DynamicSelectQuery selectByExample = builder.create() //
      .literal("SELECT id, name, type, balance\n") //
      .literal("FROM account") //
      .where("AND", builder.ifs() //
          .ifPart("f.id != null", builder.create().literal("id = ").parameter("f.id", Types.NUMERIC).end())
          .ifPart("f.name != null", builder.create().literal("name = ").parameter("f.name", Types.VARCHAR).end())
          .ifPart("f.type != null", builder.create().literal("type = ").parameter("f.type", Types.VARCHAR).end())
          .ifPart("f.balance != null",
              builder.create().literal("balance = ").parameter("f.balance", Types.NUMERIC).end())
          .end() //
      ).endSelectQuery();

  public List<Account> select(Connection conn, Account filter) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("f", filter);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedSelectQuery<Account> preparedQuery = this.selectByExample.prepare(context, Account.class);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    RowReader<Account> rowReader = new RowReader<Account>() {

      @Override
      public Account readRowFrom(ResultSet rs) throws SQLException {
        Account row = new Account();
        row.setId(rs.getInt(1));
        row.setName(rs.getString(2));
        row.setType(rs.getString(3));
        row.setBalance(rs.getInt(4));
        return row;
      }

    };

    List<Account> accounts = preparedQuery.execute(conn, rowReader);

    return accounts;
  }

  // ====================
  // === NO RETRIEVAL ===
  // ====================

//  private final DynamicInsertQuery insert = builder.create() //
//      .literal("INSERT INTO account (") //
//      .ifPart("n.id != null", builder.create().literal("id, ").end()).literal("name, type, balance)\n") //
//      .literal("VALUES (") //
//      .ifPart("n.id != null", builder.create().parameter("n.id", Types.NUMERIC).literal(", ").end())
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
//  private static final InsertProperties ORACLE_IDENTITY_INSERT_PROPERTIES = new InsertProperties(null, null, "id");
//  private final DynamicInsertQuery insert = builder.create() //
//      .literal("INSERT INTO account (") //
//      .literal("name, type, balance)\n") //
//      .literal("VALUES (") //
//      .parameter("n.name", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.type", Types.VARCHAR) //
//      .literal(", ") //
//      .parameter("n.balance", Types.NUMERIC) //
//      .literal(")") //
//      .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET, ORACLE_IDENTITY_INSERT_PROPERTIES);

//  // DB2, PostgreSQL, SQL Server, MySQL, MariaDB, Sybase ASE, H2, HyperSQL, and Derby - Identity Inline
//  private final DynamicInsertQuery insert = builder.create() //
//      .literal("INSERT INTO account (") //
//      .ifPart("n.id != null", builder.create().literal("id, ").end()).literal("name, type, balance)\n") //
//      .literal("VALUES (") //
//      .ifPart("n.id != null", builder.create().parameter("n.id", Types.NUMERIC).literal(", ").end())
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
//  private static final InsertProperties ORACLE_SEQUENCE_INLINE_INSERT_PROPERTIES = new InsertProperties(null, null,
//      "id");
//  private final DynamicInsertQuery insert = builder.create() //
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
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET, ORACLE_SEQUENCE_INLINE_INSERT_PROPERTIES);

//  // DB2 - Sequence Inline
//  private final DynamicInsertQuery insert = builder.create() //
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
//      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET);

//  // PostgreSQL - Sequence Inline
//  private final DynamicInsertQuery insert = builder.create() //
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
//  private final DynamicInsertQuery insert = builder.create() //
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

//  // H2, HyperSQL, and Derby - Sequence Inline
//  private final DynamicInsertQuery insert = builder.create() //
//    .literal("INSERT INTO account (") //
//    .literal("  id, name, type, balance\n") //
//    .literal(") VALUES (\n  ") //
//    .literal("NEXT VALUE FOR seq_account") //
//    .literal(", ") //
//    .parameter("n.name", Types.VARCHAR) //
//    .literal(", ") //
//    .parameter("n.type", Types.VARCHAR) //
//    .literal(", ") //
//    .parameter("n.balance", Types.NUMERIC) //
//    .literal(")") //
//    .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET);

  // =========================
  // === SEQUENCE PREFETCH ===
  // =========================

//  // Oracle - Sequence Prefetch
//  private static final InsertProperties ORACLE_SEQUENCE_INSERT_PROPERTIES = new InsertProperties(
//      "SELECT seq_account.NEXTVAL FROM DUAL", "n.id");
//  private final DynamicInsertQuery insert = builder.create() //
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
//  private final DynamicInsertQuery insert = builder.create() //
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
//  private final DynamicInsertQuery insert = builder.create() //
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
//  private final DynamicInsertQuery insert = builder.create() //
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
//  private final DynamicInsertQuery insert = builder.create() //
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

  // HyperSQL, and Derby sequence Prefetch
  private static final InsertProperties H2_IDENTITY_INSERT_PROPERTIES = new InsertProperties(
      "VALUES NEXT VALUE FOR seq_account", "n.id");
  private final DynamicInsertQuery insert = builder.create() //
      .literal("INSERT INTO account (\n") //
      .literal("  id, name, type, balance\n") //
      .literal(") VALUES (\n  ") //
      .parameter("n.id", Types.NUMERIC).literal(", ") //
      .parameter("n.name", Types.VARCHAR) //
      .literal(", ") //
      .parameter("n.type", Types.VARCHAR) //
      .literal(", ") //
      .parameter("n.balance", Types.NUMERIC) //
      .literal(")") //
      .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_PREFETCH, H2_IDENTITY_INSERT_PROPERTIES);

  public void insert(Connection conn, Account entity) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("n", entity);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedInsertQuery preparedQuery = this.insert.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    Long id = preparedQuery.execute(conn);
    entity.setId(id == null ? null : id.intValue());

  }

  private final DynamicModificationQuery updateByExample = builder.create() //
      .literal("UPDATE account") //
      .set(builder.ifs() //
          .ifPart("n.id != null", builder.create().literal("id = ").parameter("n.id", Types.NUMERIC).end())
          .ifPart("n.name != null", builder.create().literal("name = ").parameter("n.name", Types.VARCHAR).end())
          .ifPart("n.type != null", builder.create().literal("type = ").parameter("n.type", Types.VARCHAR).end())
          .ifPart("n.balance != null",
              builder.create().literal("balance = ").parameter("n.balance", Types.NUMERIC).end()) //
          .end() //
      ).where("AND", builder.ifs() //
          .ifPart("f.id != null", builder.create().literal("id = ").parameter("f.id", Types.NUMERIC).end())
          .ifPart("f.name != null", builder.create().literal("name = ").parameter("f.name", Types.VARCHAR).end())
          .ifPart("f.type != null", builder.create().literal("type = ").parameter("f.type", Types.VARCHAR).end())
          .ifPart("f.balance != null",
              builder.create().literal("balance = ").parameter("f.balance", Types.NUMERIC).end())
          .end() //
      ).endModificationQuery();

  public int update(Connection conn, Account filter, Account newValues)
      throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("f", filter);
    context.add("n", newValues);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedModificationQuery preparedQuery = this.updateByExample.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

  private final DynamicModificationQuery deleteByExample = builder.create() //
      .literal("DELETE FROM account") //
      .where("AND", builder.ifs() //
          .ifPart("f.id != null", builder.create().literal("id = ").parameter("f.id", Types.NUMERIC).end())
          .ifPart("f.name != null", builder.create().literal("name = ").parameter("f.name", Types.VARCHAR).end())
          .ifPart("f.type != null", builder.create().literal("type = ").parameter("f.type", Types.VARCHAR).end())
          .ifPart("f.balance != null",
              builder.create().literal("balance = ").parameter("f.balance", Types.NUMERIC).end())
          .end() //
      ).endModificationQuery();

  public int delete(Connection conn, Account filter) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("f", filter);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedModificationQuery preparedQuery = this.deleteByExample.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

}
