package org.hotrod.runtime.livesql;

import java.util.List;

import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;

public interface LayerConfig {

  List<TypeRule> getTypeRules();
}
