package org.hotrod.runtime.sql.dialects;

public interface FunctionTranslator {

  String getLog();

  String getPow();

  String getRound();

  String getSignum();

  String getCoalesce();
}
