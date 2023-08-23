package org.hotrod.torcs.rankings;

import java.util.List;

public interface Ranking {

  String getTitle();

  void clear();

  void add(Query q);

  List<Query> list();

}
