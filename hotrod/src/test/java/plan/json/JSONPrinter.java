package plan.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONPrinter {

  public static void main(final String[] args) throws IOException {
    System.out.println("1");
    String json = file2String("docs/research/sql-optimizer/mysql/mysql8-json.json");
    System.out.println("1b: " + json);

    JsonElement root = new JsonParser().parse(json);
    JsonObject jobject = root.getAsJsonObject();
    
    JsonObject qb = jobject.getAsJsonObject("query_block");
    JsonElement sid = qb.get("select_id");
    System.out.println("sid: " + sid.getAsString());
    
    

  }

  // Helpers

  private static String file2String(final String fileName) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader r = new BufferedReader(new FileReader(new File(fileName)));
    String line = null;
    while ((line = r.readLine()) != null) {
      sb.append(line);
      sb.append("\n");
    }
    return sb.toString();
  }

}
