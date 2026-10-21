package manual;

import java.time.LocalDate;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.jexl.JEXLDynamicExpressionFactory;

public class ObjectContextCheck {

  private static final String[] ALLOWED_PACKAGES = new String[] {};

  public static void main(String[] args) {

    // Parameters

    String myExpr = "precision < 2";
    MyWidget w = new MyWidget(1005, "'Daguerrotype", LocalDate.of(2024, 12, 31));

    // Evaluate

    JEXLDynamicExpressionFactory factory = new JEXLDynamicExpressionFactory(ALLOWED_PACKAGES);

    DynamicExpression ex = factory.expression(myExpr);

    Parameters ctx = factory.newObjectContext(w);
    Object v = ex.evaluate(ctx);

    System.out.println("v=" + v);

  }

  public static class MyWidget {
    private Integer id;
    private String name;
    private LocalDate today;

    public MyWidget(Integer id, String name, LocalDate today) {
      this.id = id;
      this.name = name;
      this.today = today;
    }

    public Integer getId() {
      return id;
    }

    public Integer getPrecision() {
      return id;
    }

    public String getName() {
      return name;
    }

    public LocalDate getToday() {
      return today;
    }

  }

}
