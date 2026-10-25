package org.hotrod.torcs.rankings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.util.List;

import javax.sql.DataSource;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.QueryExecution;
import org.junit.jupiter.api.Test;

class RankingsTest {

  private final DataSourceReference dataSource = DataSourceReference.of((DataSource) Proxy.newProxyInstance(
      DataSource.class.getClassLoader(), new Class<?>[] { DataSource.class }, (proxy, method, args) -> defaultValue(method.getReturnType())));

  @Test
  void highestFrequencyRanksByExecutionCountAndLimitsResults() {
    HighestFrequencyRanking ranking = new HighestFrequencyRanking(2, 3);
    ranking.apply(execution("one", 10));
    ranking.apply(execution("two", 20));
    ranking.apply(execution("two", 21));
    ranking.apply(execution("two", 22));
    ranking.apply(execution("three", 30));

    List<RankingEntry> entries = ranking.getEntries();
    assertEquals(2, entries.size());
    assertEquals("two", entries.get(0).getSQL());
    assertEquals(3, entries.get(0).getExecutions());
    assertEquals("one", entries.get(1).getSQL());
  }

  @Test
  void highestResponseTimeMovesAnExistingEntryWhenItBecomesSlower() {
    HighestResponseTimeRanking ranking = new HighestResponseTimeRanking(2);
    ranking.apply(execution("fast", 10));
    ranking.apply(execution("slow", 20));
    ranking.apply(execution("fast", 30));

    List<RankingEntry> entries = ranking.getEntries();
    assertEquals("fast", entries.get(0).getSQL());
    assertEquals(2, entries.get(0).getExecutions());
    assertEquals(30, entries.get(0).getMaxTime());
  }

  @Test
  void highestImpactRanksByCumulativeResponseTime() {
    HighestImpactRanking ranking = new HighestImpactRanking(3);
    ranking.apply(execution("first", 40));
    ranking.apply(execution("second", 30));
    ranking.apply(execution("second", 20));
    ranking.apply(execution("third", 45));

    List<RankingEntry> entries = ranking.getEntries();
    assertEquals("second", entries.get(0).getSQL());
    assertEquals(2, entries.get(0).getExecutions());
    assertEquals(50, entries.get(0).getImpact());
    assertEquals("third", entries.get(1).getSQL());
    assertEquals("first", entries.get(2).getSQL());
  }

  @Test
  void latestRankingEvictsTheOldestDistinctQuery() {
    LatestQueriesRanking ranking = new LatestQueriesRanking(2);
    ranking.apply(execution("first", 1));
    ranking.apply(execution("second", 2));
    ranking.apply(execution("third", 3));

    assertEquals(2, ranking.getEntries().size());
    assertEquals("second", ranking.getEntries().iterator().next().getSQL());
  }

  @Test
  void rankingSizesAreValidated() {
    assertThrows(RuntimeException.class, () -> new HighestFrequencyRanking(0));
    assertThrows(RuntimeException.class, () -> new HighestResponseTimeRanking(1001));
    assertThrows(RuntimeException.class, () -> new LatestQueriesRanking(0));
  }

  private QueryExecution execution(String sql, int responseTime) {
    return new QueryExecution(dataSource, sql, null, null, responseTime, null);
  }

  private static Object defaultValue(Class<?> type) {
    if (!type.isPrimitive()) {
      return null;
    }
    if (type == boolean.class) {
      return false;
    }
    if (type == byte.class) {
      return (byte) 0;
    }
    if (type == short.class) {
      return (short) 0;
    }
    if (type == int.class) {
      return 0;
    }
    if (type == long.class) {
      return 0L;
    }
    if (type == float.class) {
      return 0F;
    }
    if (type == double.class) {
      return 0D;
    }
    if (type == char.class) {
      return '\0';
    }
    return null;
  }
}
