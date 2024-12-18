package test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.ObjectContext;
import org.apache.commons.jexl3.introspection.JexlPermissions;

public class T51 {

//  public static JexlEngine JEXL_ENGINE = new JexlBuilder().cache(100).strict(true).debug(true).silent(false).create();
//  public static JexlEngine JEXL_ENGINE2 = new JexlBuilder().permissions(JexlPermissions.UNRESTRICTED).create();

  public static void main(String[] args) {

    // Allowing: lines that end with .*
    // Restricted: anything else
    // See: https://commons.apache.org/proper/commons-jexl/apidocs/org/apache/commons/jexl3/introspection/JexlPermissions.html#parse(java.lang.String...)
    JexlPermissions permissions = JexlPermissions.parse("app.*\n" //
        + "java.math.*\n" //
        + "java.text.*\n" //
        + "java.util.*\n" //
        + "java.lang { Runtime {} System {} ProcessBuilder {} Class {} }\n" //
        + "org.apache.commons.jexl3 { JexlBuilder {} }" + "");

    JexlEngine engine = new JexlBuilder().permissions(permissions).create();

    JexlExpression expr = engine.createExpression("name");
    ObjectContext<Task> ctx = new ObjectContext<>(engine, new Task("Task5", 500));
//    MyContext ctx = new MyContext();
    Object obj = expr.evaluate(ctx);
    System.out.println("obj=" + obj);
  }

  public static class Task {

    public String name;
    public int price;

    public Task(String name, int price) {
      this.name = name;
      this.price = price;
    }

    public String getName() {
      return name;
    }

    public int getPrice() {
      return price;
    }

  }

  public static class MyContext implements JexlContext {

    public Map<String, Object> vars = new HashMap<>();

    public MyContext() {
      this.vars.put("a", new Task("Task1", 100));
      this.vars.put("b", new Task("Task2", 200));
      this.vars.put("c", new Task("Task3", 300));
      this.vars.put("", new Task("Task4", 400));
    }

    @Override
    public Object get(String name) {
      System.out.println("MyContext.get('" + name + "')");
      return this.vars.get(name);
    }

    @Override
    public boolean has(String name) {
      System.out.println("MyContext.has('" + name + "')");
      return this.vars.containsKey(name);
    }

    @Override
    public void set(String name, Object value) {
      System.out.println("MyContext.set('" + name + "', '" + value + "')");
    }

  }

}
