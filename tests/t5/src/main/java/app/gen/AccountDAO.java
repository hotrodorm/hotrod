package app.gen;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.DynamicSelectQuery;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;
import org.hotrod.dynamic.builder.QueryBuilder;

public class AccountDAO {

  private final DynamicExpressionFactory factory = DynamicExpressionFactory.getFactory();
  private final QueryBuilder builder = new QueryBuilder(this.factory);

  private final DynamicSelectQuery selectByExample = builder.create() //
      .literal("SELECT * FROM account") //
      .where(builder.ifsSegments() //
          .ifSegment("f.id != null", builder.create().literal("and id = ").parameter("f.id", Types.NUMERIC).end())
          .ifSegment("f.name != null", builder.create().literal("and name = ").parameter("f.name", Types.VARCHAR).end())
          .ifSegment("f.type != null", builder.create().literal("and type = ").parameter("f.type", Types.VARCHAR).end())
          .ifSegment("f.balance != null",
              builder.create().literal("and balance = ").parameter("f.balance", Types.NUMERIC).end())
          .end() //
      ).endSelectQuery();

  public int select(Connection conn, Account filter) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("f", filter);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedQuery preparedQuery = this.selectByExample.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

  private final DynamicModificationQuery updateByExample = builder.create() //
      .literal("UPDATE account") //
      .set(builder.ifsSegments() //
          .ifSegment("n.id != null", builder.create().literal("id = ").parameter("n.id", Types.NUMERIC).end())
          .ifSegment("n.name != null", builder.create().literal("name = ").parameter("n.name", Types.VARCHAR).end())
          .ifSegment("n.type != null", builder.create().literal("type = ").parameter("n.type", Types.VARCHAR).end())
          .ifSegment("n.balance != null",
              builder.create().literal("balance = ").parameter("n.balance", Types.NUMERIC).end()) //
          .end() //
      ).where(builder.ifsSegments() //
          .ifSegment("f.id != null", builder.create().literal("and id = ").parameter("f.id", Types.NUMERIC).end())
          .ifSegment("f.name != null", builder.create().literal("and name = ").parameter("f.name", Types.VARCHAR).end())
          .ifSegment("f.type != null", builder.create().literal("and type = ").parameter("f.type", Types.VARCHAR).end())
          .ifSegment("f.balance != null",
              builder.create().literal("and balance = ").parameter("f.balance", Types.NUMERIC).end())
          .end() //
      ).endModificationQuery();

  public int update(Connection conn, Account filter, Account newValues)
      throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("f", filter);
    context.add("n", newValues);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedQuery preparedQuery = this.updateByExample.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

  private final DynamicModificationQuery deleteByExample = builder.create() //
      .literal("DELETE FROM account") //
      .where(builder.ifsSegments() //
          .ifSegment("f.id != null", builder.create().literal("and id = ").parameter("f.id", Types.NUMERIC).end())
          .ifSegment("f.name != null", builder.create().literal("and name = ").parameter("f.name", Types.VARCHAR).end())
          .ifSegment("f.type != null", builder.create().literal("and type = ").parameter("f.type", Types.VARCHAR).end())
          .ifSegment("f.balance != null",
              builder.create().literal("and balance = ").parameter("f.balance", Types.NUMERIC).end())
          .end() //
      ).endModificationQuery();

  public int delete(Connection conn, Account filter) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("f", filter);

    // 2. Process DynamicSQL and produce query and parameters

    PreparedQuery preparedQuery = this.deleteByExample.prepare(context);
    System.out.println("=== Preview ===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

}
