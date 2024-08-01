package org.hotrod.config.structuredcolumns;

import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.ColumnsPrefixGenerator;

public interface ColumnsProvider {

  void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, ColumnsRetriever cr)
      throws InvalidSQLException, InvalidConfigurationFileException;

  void gatherMetadataPhase2() throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException,
      InvalidConfigurationFileException;

  String renderColumns();

}
