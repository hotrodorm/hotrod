package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.metadata.TableOrView;

public abstract class NaturalJoin extends Join {

  NaturalJoin(final TableOrView table) {
    super(table);
  }

}
