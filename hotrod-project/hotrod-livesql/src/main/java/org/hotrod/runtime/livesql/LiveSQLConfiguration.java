package org.hotrod.runtime.livesql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration(proxyBeanMethods = false)
public class LiveSQLConfiguration {

  @Value("${use.plain.jdbc:false}")
  private boolean usePlainJDBC;

  public boolean usePlainJDBC() {
    return usePlainJDBC;
  }

}
