
# Generating HotRod persistence layer

```bash
mvn -P mysql hotrod:gen
mvn -P postgresql hotrod:gen
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

