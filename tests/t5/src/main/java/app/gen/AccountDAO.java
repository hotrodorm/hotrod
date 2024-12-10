package app.gen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.DynamicSelectQuery;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedModificationQuery;
import org.hotrod.dynamic.PreparedSelectQuery;
import org.hotrod.dynamic.PreparedSelectQuery.RowReader;
import org.hotrod.dynamic.builder.QueryBuilder;

public class AccountDAO {

  private final DynamicExpressionFactory factory = DynamicExpressionFactory.getFactory();
  private final QueryBuilder builder = new QueryBuilder(this.factory);

  private final DynamicSelectQuery selectByExample = builder.create() //
      .literal("SELECT id, name, type, balance\nFROM account") //
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
