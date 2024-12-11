package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.insert.InsertExecutor;
import org.hotrod.dynamic.insert.InsertProperties;
import org.hotrod.dynamic.insert.PreparedInsertQuery;
import org.hotrod.dynamic.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicInsertQuery extends DynamicQuery {

  private PrimaryKeyRetrievalMode primaryKeyRetrievalMode;
  private InsertProperties insertProperties;

  public DynamicInsertQuery(List<QuerySegment> segments, PrimaryKeyRetrievalMode primaryKeyRetrievalMode,
      InsertProperties insertProperties) {
    super(segments);
    this.primaryKeyRetrievalMode = primaryKeyRetrievalMode;
    this.insertProperties = insertProperties;
  }

  public PreparedInsertQuery prepare(ParameterContext context) throws DynamicExpressionException {
    InsertExecutor executor = this.primaryKeyRetrievalMode.getInsertExecutor();
    PreparedInsertQuery pq = new PreparedInsertQuery(executor, this.insertProperties);
    for (QuerySegment s : this.segments) {
      s.prepare(pq, context);
    }
    return pq;
  }

}
