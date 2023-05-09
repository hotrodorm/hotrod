package test.cache;

import org.hotrod.torcs.Statement;

public class TestCache {

  public static void main(String[] args) {

    Cache<Statement> maxTimeCache = new Cache<Statement>(10, (a, b) -> a.getMaxTime() < b.getMaxTime());

  }

}
