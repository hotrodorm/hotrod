package manual;

import java.sql.Types;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.assembler.QueryAssembler;

public class ParameterTesting {

  public static void main(String[] args) throws DynamicExpressionException {

    QueryAssembler assembler = new QueryAssembler(DynamicExpressionFactoryConfig.getFactory());

    DynamicSelectQuery q = assembler.literal("SELECT * FROM T WHERE a = ").parameter("id", Types.INTEGER)
        .endSelectQuery();

    ParameterContext ctx = assembler.newParameterContext();
    ctx.add("id", 123);

    PreparedSelectQuery<Integer> pq = q.prepare(ctx, Integer.class);

    String preview = pq.getPreview(true);

    System.out.println("query:\n" + preview);

//    List<Integer> data = pq.execute(conn, rowReader);

  }

}
