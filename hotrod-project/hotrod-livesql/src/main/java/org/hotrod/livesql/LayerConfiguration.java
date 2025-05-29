package org.hotrod.livesql;

import java.util.List;

import org.hotrod.livesql.queries.typesolver.TypeRule;

public interface LayerConfiguration {

  List<TypeRule> getTypeRules();
}
