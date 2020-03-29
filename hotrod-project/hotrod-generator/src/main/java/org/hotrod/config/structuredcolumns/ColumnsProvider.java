package org.hotrod.config.structuredcolumns;

import java.sql.Connection;

import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.utils.ColumnsPrefixGenerator;

public interface ColumnsProvider {

  // void validateAgainstDatabase(final HotRodGenerator generator) throws
  // InvalidConfigurationFileException;

  void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, Connection conn1) throws InvalidSQLException;

  void gatherMetadataPhase2(final Connection conn2, final TypeSolverTag typeSolverTag) throws InvalidSQLException, UncontrolledException,
      UnresolvableDataTypeException, ControlledException, InvalidConfigurationFileException;

  String renderColumns();

}
