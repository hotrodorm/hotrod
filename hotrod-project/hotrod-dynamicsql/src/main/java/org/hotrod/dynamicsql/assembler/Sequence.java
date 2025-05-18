package org.hotrod.dynamicsql.assembler;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;

public class Sequence extends Sentence<Sequence, Sequence> {

  private static final Logger log = Logger.getLogger(Sequence.class.getName());

  public Sequence(DynamicExpressionFactory factory) {
    super(factory, null, null);
    log.fine("init");
    super.setMe(this);
  }

  public DynamicModificationQuery endModificationQuery() {
    return new DynamicModificationQuery(super.factory, this.segments);
  }

  public DynamicSelectQuery endSelectQuery() {
    DynamicSelectQuery q = new DynamicSelectQuery(super.factory, this.segments);
    return q;
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode) {
    return new DynamicInsertQuery(super.factory, this.segments, primaryKeyRetrievalMode, null, null, null);
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode, String sequencePreFetchSQL,
      String primaryKeyParameterName, String... generatedKeysNames) {
    return new DynamicInsertQuery(super.factory, this.segments, primaryKeyRetrievalMode, sequencePreFetchSQL,
        primaryKeyParameterName, generatedKeysNames);
  }

}
