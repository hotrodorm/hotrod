package org.hotrod.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.hotrod.exceptions.PersistenceException;
import org.hotrod.exceptions.StaleDataException;
import org.hotrod.typesolver.DriverColumnMetaData;
import org.hotrod.typesolver.TypeSolverConst;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.ConverterUtil;
import org.junit.jupiter.api.Test;

public class CoreContractTest {

  @Test
  public void converterUtilityInvokesEncode() {
    TypeConverter<Integer, String> converter = new TypeConverter<Integer, String>() {
      public String decode(Integer raw, Connection connection) { return String.valueOf(raw); }
      public Integer encode(String domain, Connection connection) { return domain.length(); }
    };
    assertEquals(Integer.valueOf(4), ConverterUtil.encode("test", converter, null));
  }

  @Test
  public void converterExceptionsAreWrapped() {
    TypeConverter<Integer, String> converter = new TypeConverter<Integer, String>() {
      public String decode(Integer raw, Connection connection) { return ""; }
      public Integer encode(String domain, Connection connection) { throw new IllegalStateException("failed"); }
    };
    try {
      ConverterUtil.encode("test", converter, null);
    } catch (PersistenceException e) {
      assertTrue(e.getMessage().contains("threw an exception"));
      assertTrue(e.getCause() != null);
      return;
    }
    throw new AssertionError("Expected PersistenceException");
  }

  @Test
  public void exceptionConstructorsPreserveMessagesAndCauses() {
    Throwable cause = new IllegalArgumentException("cause");
    PersistenceException exception = new PersistenceException("message", cause);
    assertEquals("message", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertEquals("stale", new StaleDataException("stale").getMessage());
  }

  @Test
  public void typeMetadataExceptionPreservesColumnMetadata() {
    DriverColumnMetaData metadata = new DriverColumnMetaData() {
      public String getCatalog() { return null; }
      public String getSchema() { return "public"; }
      public String getObjectType() { return "TABLE"; }
      public String getTable() { return "users"; }
      public int getOrdinal() { return 1; }
      public String getName() { return "id"; }
      public String getLabel() { return "id"; }
      public String getTypeName() { return "INTEGER"; }
      public Integer getDataType() { return 4; }
      public String getDriverDefaultClassName() { return "java.lang.Integer"; }
      public Integer getDisplaySize() { return 10; }
      public Integer getPrecision() { return 10; }
      public Integer getScale() { return 0; }
    };
    UnresolvableDataTypeException exception = new UnresolvableDataTypeException(metadata, "unknown");
    assertSame(metadata, exception.getColumnMetadata());
    assertEquals("unknown", exception.getMessage());
    assertEquals("RD", TypeSolverConst.RUNTIME_DIALECT_NAMESPACE);
    assertEquals("RT", TypeSolverConst.RUNTIME_TYPESOLVER_NAMESPACE);
  }
}
