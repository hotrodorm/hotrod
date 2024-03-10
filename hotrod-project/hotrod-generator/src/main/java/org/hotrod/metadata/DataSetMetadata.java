package org.hotrod.metadata;

import java.io.Serializable;
import java.util.List;

import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.ClassicFKNavigationTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.identifiers.ObjectId;

public interface DataSetMetadata extends Serializable {

  AbstractDAOTag getDaoTag();

  List<ColumnMetadata> getColumns();

  List<ColumnMetadata> getNonPkColumns();

  KeyMetadata getPK();

  ObjectId getId();

  List<KeyMetadata> getUniqueIndexes();

  List<ForeignKeyMetadata> getImportedFKs();

  List<ForeignKeyMetadata> getExportedFKs();

  List<SelectParameterMetadata> getParameters();

  List<SelectParameterMetadata> getParameterDefinitions();

  String renderSQLSentence(ParameterRenderer parameterRenderer);

  String renderXML(ParameterRenderer parameterRenderer);

  default public ClassicFKNavigationTag getClassicFKNavigation() {
    return null;
  }

  VersionControlMetadata getVersionControlMetadata();

  HotRodFragmentConfigTag getFragmentConfig();

  List<SelectMethodMetadata> getSelectsMetadata();

  default public TableDataSetMetadata getParentMetadata() {
    return null;
  }

}
