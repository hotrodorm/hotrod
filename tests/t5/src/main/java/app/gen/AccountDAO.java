package app.gen;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicModificationQuery;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;
import org.hotrod.dynamic.builder.QueryBuilder;

public class AccountDAO {

  private final DynamicExpressionFactory factory = DynamicExpressionFactory.getFactory();
  private final QueryBuilder builder = new QueryBuilder(this.factory);

//  private final DynamicModificationQuery update = builder.create() //
//      .literal("UPDATE account \nSET balance = ") //
//      .parameter("balance", Types.NUMERIC) //
//      .ifSegment("id != null", builder.create() //
//          .literal("\nWHERE id = ") //
//          .parameter("id", Types.NUMERIC) //
//          .end() //
//      ).endModificationQuery();

  private final DynamicModificationQuery update = builder.create() //
      .literal("UPDATE account \nSET balance = ") //
      .parameter("n.balance", Types.NUMERIC) //
      .whereSegment(builder.ifSegments() //
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
//    context.add("id", filter.getId());
//    context.add("name", filter.getName());
//    context.add("type", filter.getType());
//    context.add("balance", filter.getBalance());

    // 2. Process DynamicSQL and produce query and parameters

    PreparedQuery preparedQuery = this.update.prepare(context);
    System.out.println("=== Preview===\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

}
