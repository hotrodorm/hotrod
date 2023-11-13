package org.hotrod.torcs.plan;

import java.sql.SQLException;

import org.hotrod.torcs.rankings.RankingEntry;

public interface TorcsPlanRetriever {

  String getEstimatedExecutionPlan(RankingEntry entry) throws SQLException;

}
