package org.hotrod.torcs.rankings;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.hotrod.torcs.QuerySampleObserver;

public abstract class Ranking extends QuerySampleObserver {

  public abstract Collection<RankingEntry> getRanking();

  public void saveAsXLSX(final OutputStream os) throws IOException {
    XLSXRankingWriter.writeTo(this, os);
  }

}
