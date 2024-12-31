package org.hotrod.dynamicsql;

import java.util.List;

import org.hotrod.dynamicsql.insert.PreparedInsertQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamicsql.segments.QuerySegment;

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
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedInsertQuery(sc, this.primaryKeyRetrievalMode, this.sequencePreFetchSQL,
        this.primaryKeyParameterName, this.generatedKeysNames);
  }

}
