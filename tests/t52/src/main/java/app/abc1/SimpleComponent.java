package app.abc1;

import org.springframework.stereotype.Component;

@Component("MySimpleComponent")
public class SimpleComponent {

  public String action() {
    return "mySimpleComponent";
  }

}
