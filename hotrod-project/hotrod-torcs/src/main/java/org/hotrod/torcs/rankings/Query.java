package org.hotrod.torcs.rankings;

public interface Query {

  String getSQL();

  int getResponseTime();

  String getPlan();

}
