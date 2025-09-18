package org.hotrod.config.structuredcolumns;

import org.hotrod.config.SelectMethodTag;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.utils.ColumnsPrefixGenerator;

public interface ColumnsProvider {

  void gatherMetadataPhase1(final SelectMethodTag selectTag, final ColumnsPrefixGenerator columnsPrefixGenerator,
      ColumnsRetriever cr) throws FaultException, ErrorMessageException;

  void gatherMetadataPhase2() throws FaultException, ErrorMessageException;

  String renderColumns();

}
