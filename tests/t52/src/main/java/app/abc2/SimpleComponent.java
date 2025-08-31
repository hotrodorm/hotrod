package app.abc2;

import org.springframework.stereotype.Component;

@Component("MySimpleComponent")
public class SimpleComponent {

  public String action() {
    return "mySimpleComponent";
  }

}
