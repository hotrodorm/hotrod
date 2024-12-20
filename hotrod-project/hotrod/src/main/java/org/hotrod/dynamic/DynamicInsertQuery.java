package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.insert.PreparedInsertQuery;
import org.hotrod.dynamic.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicInsertQuery extends DynamicQuery {

  private PrimaryKeyRetrievalMode primaryKeyRetrievalMode;
  private String sequencePreFetchSQL;
  private String primaryKeyParameterName;
  private String[] generatedKeysNames;

  public DynamicInsertQuery(List<QuerySegment> segments, PrimaryKeyRetrievalMode primaryKeyRetrievalMode,
      String sequencePreFetchSQL, String primaryKeyParameterName, String[] generatedKeysNames) {
    super(segments);
    this.primaryKeyRetrievalMode = primaryKeyRetrievalMode;
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.primaryKeyParameterName = primaryKeyParameterName;
    this.generatedKeysNames = generatedKeysNames;
  }

  public PreparedInsertQuery prepare(ParameterContext context) throws DynamicExpressionException {
    PreparedInsertQuery pq = new PreparedInsertQuery(this.primaryKeyRetrievalMode, this.sequencePreFetchSQL,
        this.primaryKeyParameterName, this.generatedKeysNames);
    for (QuerySegment s : this.segments) {
      s.prepare(pq, context, 0);
    }
    return pq;
  }

}
