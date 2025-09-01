package plan.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import explain.mysql8.MySQL8Plan;

public class JSONPrinter {

  public static void main(final String[] args) throws IOException {
    System.out.println("1");
    // String json =
    // file2String("docs/research/sql-optimizer/mysql/mysql8-json.json");
    // String json = file2String("src/test/java/plan/json/t1.json");
    String json = file2String("src/test/java/plan/json/mysql8.json");
    System.out.println("1b: " + json);

    // Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // String fileData = new String(Files.readAllBytes(Paths
    // .get("employee.txt")));
    Gson gson = new GsonBuilder().create();

    // Employee emp1 = gson.fromJson(json, Employee.class);
    MySQL8Plan emp1 = gson.fromJson(json, MySQL8Plan.class);
    // print object data
    System.out.println("\n\nEmployee Object\n\n" + emp1);

    // JsonElement root = new JsonParser().parse(json);
    // JsonObject jobject = root.getAsJsonObject();
    //
    // JsonObject qb = jobject.getAsJsonObject("query_block");
    // JsonElement sid = qb.get("select_id");
    // System.out.println("sid: " + sid.getAsString());

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
