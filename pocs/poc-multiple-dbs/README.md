
# Generating HotRod persistence layer

```bash
mvn -P mysql hotrod:gen
mvn -P postgresql hotrod:gen
```


```
+- LiveSQL (@Component, prototype?)
  +- SqlSession (autowired?)
  +- LiveSQLDialect (autowired?)
  +- LiveSQLMapper (autowired?)
```

## Bean Instantiation Examples

```java
package p1;
@Component
public class L { }

--- Case #1: "Singleton" by @ComponentScan - No Qualifier -----------------

package app;
@ComponentScan(basePackageClasses = L.class)
public class App {
  @Autowired
  private L l; // Succeeds: finds bean "l" from @ComponentScan
}

--- Case #2: "Singleton" by @ComponentScan - Searched with Qualifier -----------------

package app;
@ComponentScan(basePackageClasses = L.class)
public class App {
  @Autowired
  @Qualifier("l2")
  private L l; // Fails: No bean "l2" found. Only bean "l" exists.
}

--- Case #3: Bean by @Configuration - No Qualifier -----------------

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  private L l; // Succeeds: finds bean "l2" (different from "l") and uses it here.
               // Matches a different bean name only when there's a single bean of the class.
}

--- Case #4: Bean by @Configuration - Bad Qualifier -----------------

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  @Qualifier("l1")
  private L l; // Fails: No bean "l1" found. Only bean "l2" exists.
}

--- Case #5: Bean by @Configuration - Good Qualifier -----------------

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  @Qualifier("l2")
  private L l; // Succeeds: finds "l2". Only bean "l2" exists.
}

--- Case #6: "Singleton" by @ComponentScan + Well-qualified Extra Instance by @Configuration ---

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = L.class)
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  private L l; // Succeeds: finds bean "l" from @ComponentScan

  @Autowired
  @Qualifier("l2")
  private L l2; // Succeeds: finds "l2" (different instance) from @Configuration
}

--- Case #7: "Singleton" by @ComponentScan + Badly-qualified Extra Instance by @Configuration ---

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = L.class)
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  private L l; // Succeeds: finds bean "l" from @ComponentScan

  @Autowired
  @Qualifier("l3")
  private L l2; // Fails: No bean "l3" found
}

--- Case #8: "Singleton" by @ComponentScan + Well-named Extra Instance by @Configuration ---

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = L.class)
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  private L l; // Succeeds: finds bean "l" from @ComponentScan

  @Autowired
  private L l2; // Succeeds: finds "l2" (different instance) from @Configuration
}

--- Case #9: "Singleton" by @ComponentScan + Badly-named Extra Instance by @Configuration ---

package p2;
@Configuration
public class LConfig {
  @Bean
  public L l2() { return new L(); }
}

package app;
@ComponentScan(basePackageClasses = L.class)
@ComponentScan(basePackageClasses = LConfig.class)
public class App {
  @Autowired
  private L l; // Succeeds: finds bean "l" from @ComponentScan

  @Autowired
  private L l3; // Fails: No bean "l3" found.
}

```


## Dependencies

```
+- DAO1
   +- sqlSession1
      +- sqlSessionFactory1
         +- dataSource1
         +- (mappers1)
            +- ${datasource1.mappers}

+- LiveSQL1
   +- sqlSession1
   +- liveSQLDialect1
      +- ${datasource1.livesqldialect.name:#{null}}
   +- liveSQLMapper1
      +- sqlSessionFactory1
```

## Single Database Scenario

- For datasource #1 (one and only)

```
    @ComponentScan(basePackageClasses = LiveSQL.class)

    @Autowired
    private LiveSQL sql;
```



## Multi Database Scenario


And for datasource #2:

```
+- DAO2
   +- sqlSession2
      +- sqlSessionFactory2
         +- dataSource2
         +- (mappers2)
            +- ${datasource2.mappers}

+- LiveSQL2
   +- sqlSession2
   +- liveSQLDialect2
      +- ${datasource2.livesqldialect.name:#{null}}
   +- liveSQLMapper2
      +- sqlSessionFactory2

```

