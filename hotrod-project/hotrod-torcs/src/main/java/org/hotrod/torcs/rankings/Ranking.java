package org.hotrod.torcs.rankings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.hotrod.torcs.QueryExecutionObserver;

public abstract class Ranking extends QueryExecutionObserver {

  public abstract Collection<RankingEntry> getEntries();

  /**
   * Use getEntries() instead
   */
  @Deprecated
  public abstract Collection<RankingEntry> getRanking();

  public void saveAsXLSX(final File file) throws IOException {
    try (OutputStream os = new FileOutputStream(file)) {
      this.saveAsXLSX(os);
    }
  }

  public void saveAsXLSX(final OutputStream os) throws IOException {
    XLSXRankingWriter.writeTo(this, os);
  }

}
