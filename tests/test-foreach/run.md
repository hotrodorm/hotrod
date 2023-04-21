mvn -P h2 hotrod:gen

mvn -P h2 spring-boot:run

## Clean all generated files

rm -r src/main/java/app/daos/* && rm -r src/main/resources/mappers
