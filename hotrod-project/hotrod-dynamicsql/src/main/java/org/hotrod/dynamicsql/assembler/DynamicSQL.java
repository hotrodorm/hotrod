package org.hotrod.dynamicsql.assembler;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;

public class DynamicSQL extends Sentence<DynamicSQL, DynamicSQL> {

  private static final Logger log = Logger.getLogger(DynamicSQL.class.getName());

  public DynamicSQL() {
    super(DynamicExpressionFactoryConfig.getFactory(), null, null);
    log.fine("init");
    super.setMe(this);
  }

  public DynamicSQL(DynamicExpressionFactory factory) {
    super(factory, null, null);
    super.setMe(this);
  }

  public Parameters newParameters() {
    return this.factory.newParameterContext();
  }

  public DynamicModificationQuery endModificationQuery() {
    return new DynamicModificationQuery(this.segments);
  }

  public DynamicSelectQuery endSelectQuery() {
    return new DynamicSelectQuery(this.segments);
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode) {
    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, null, null, null);
  }

  public DynamicInsertQuery endInsertQuery(PrimaryKeyRetrievalMode primaryKeyRetrievalMode, String sequencePreFetchSQL,
      String primaryKeyParameterName, String... generatedKeysNames) {
    return new DynamicInsertQuery(this.segments, primaryKeyRetrievalMode, sequencePreFetchSQL, primaryKeyParameterName,
        generatedKeysNames);
  }

}
