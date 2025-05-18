package manual;

import java.time.LocalDate;

import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.ObjectContext;
import org.hotrod.dynamicsql.jexl.JEXLDynamicExpression;
import org.hotrod.dynamicsql.jexl.JEXLObjectParameterContext;

public class ObjectContextTest {

  public static void main(String[] args) {

    // Parameters

    String myExpr = "precision < 2";
    MyWidget w = new MyWidget(1005, "'Daguerrotype", LocalDate.of(2024, 12, 31));

    // Evaluate

    JexlExpression ex = JEXLDynamicExpression.JEXL_ENGINE.createExpression(myExpr);

    @SuppressWarnings("unused")
    ObjectContext<MyWidget> ctx = new ObjectContext<>(JEXLDynamicExpression.JEXL_ENGINE, w);
    JEXLObjectParameterContext ctx2 = JEXLObjectParameterContext
        .of(new ObjectContext<>(JEXLDynamicExpression.JEXL_ENGINE, w));

    Object v = ex.evaluate(ctx2);

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
