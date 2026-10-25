package org.hotrod.torcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hotrod.torcs.setters.index.BlobSetter;
import org.hotrod.torcs.setters.index.IntSetter;
import org.junit.jupiter.api.Test;

class QueryExecutionTest {

  @Test
  void nullSetterMapsBecomeEmptyCollections() {
    QueryExecution execution = new QueryExecution(null, "select 1", null, null, 12, null);

    assertTrue(execution.getIndexSetters().isEmpty());
    assertTrue(execution.getNameSetters().isEmpty());
    assertEquals(12, execution.getResponseTime());
    assertFalse(execution.wasConsumableParameterDetected());
    assertFalse(execution.wasLOBParameterDetected());
  }

  @Test
  void detectsLobAndConsumableParametersAcrossSetterTypes() {
    Map<Integer, org.hotrod.torcs.setters.index.IndexSetter> indexSetters = new HashMap<>();
    indexSetters.put(1, new BlobSetter(1, (java.sql.Blob) null));
    indexSetters.put(2, new IntSetter(2, 7));

    QueryExecution execution = new QueryExecution(null, "select ?", indexSetters, null, 4, null);

    assertTrue(execution.wasLOBParameterDetected());
    assertEquals("BlobSetter", execution.getLobTypeSetter());
    assertFalse(execution.wasConsumableParameterDetected());
    assertEquals(2, execution.getIndexSetters().size());
  }

  @Test
  void compactsMultilineSql() {
    assertEquals("select * from users where id = ?", QueryExecution.compactSQL(
        "  select *\n from users  \n where id = ? "));
  }
}
