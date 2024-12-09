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
      .parameter("balance", Types.NUMERIC) //
      .whereSegment(builder.ifSegments() //
          .ifSegment("id != null", builder.create().literal("and id = ").parameter("id", Types.NUMERIC).end())
          .ifSegment("name != null", builder.create().literal("and name = ").parameter("name", Types.VARCHAR).end())
          .ifSegment("type != null", builder.create().literal("and type = ").parameter("type", Types.VARCHAR).end())
          .ifSegment("balance != null", builder.create().literal("and balance = ").parameter("balance", Types.NUMERIC).end())
          .end() //
      ).endModificationQuery();

  public int update(Connection conn, Account account) throws DynamicExpressionException, SQLException {

    // 1. Prepare the parameter context

    ParameterContext context = this.factory.newParameterContext();
    context.add("id", account.getId());
    context.add("name", account.getName());
    context.add("type", account.getType());
    context.add("balance", account.getBalance());

    // 2. Process DynamicSQL and produce query and parameters

    PreparedQuery preparedQuery = this.update.prepare(context);
    System.out.println("Preview:\n" + preparedQuery.getPreview());

    // 3. Execute the resulting query

    int rows = preparedQuery.execute(conn);

    return rows;
  }

}
