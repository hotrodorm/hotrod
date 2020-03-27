package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.metadata.TableOrView;

public class NaturalLeftOuterJoin extends NaturalJoin {

  NaturalLeftOuterJoin(final TableOrView table) {
    super(table);
  }

}
