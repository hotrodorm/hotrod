package populate;

import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Build {

  public static void main(final String[] args) {
    System.out.println("Building collections...");

    try {

      // MongoClient mongoClient = new MongoClient("localhost");
      MongoClient mongoClient = new MongoClient("192.168.56.200", 27017);

      List<String> databases = mongoClient.getDatabaseNames();

      // listDatabases(mongoClient, databases);

      DB db = mongoClient.getDB("cms");
      System.out.println("Connect to database successfully");

      DBCollection c = db.getCollection("college");
      c.save(new BasicDBObject("name1", "value1"));

//      DBCollection school = db.createCollection("college", new BasicDBList());
      System.out.println("Collection mycol created successfully");

      mongoClient.close();

      // } catch (UnknownHostException ex) {
      // ex.printStackTrace();
    } finally {
      // skip
    }

    System.out.println("Collections built!");
  }

  private static void listDatabases(MongoClient mongoClient, List<String> databases) {
    for (String dbName : databases) {
      System.out.println("- Database: " + dbName);

      DB db = mongoClient.getDB(dbName);

      Set<String> collections = db.getCollectionNames();
      for (String colName : collections) {
        System.out.println("\t + Collection: " + colName);
      }
    }
  }

}
