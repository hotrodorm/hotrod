The general usage form is:

mvn archetype:generate -DinteractiveMode=false            \
  -DarchetypeGroupId=org.hotrodorm.hotrod                 \
  -DarchetypeArtifactId=hotrod-archetype-sm-jar-app       \
  -DarchetypeVersion=3.4.5-SNAPSHOT                       \
  -DgroupId=com.app1                                      \
  -DartifactId=app1                                       \
  -Dversion=1.0.0-SNAPSHOT                                \
  -Dpackage=com.myapp                                     \
  -Dpersistencepackage=persistence                        \
  -Djdbcdriverclassname=oracle.jdbc.driver.OracleDriver   \
  -Djdbcurl="jdbc:oracle:thin:@192.168.56.95:1521:orcl"   \
  -Djdbcusername=user1                                    \
  -Djdbcpassword=pass1                                    \
  -Djdbccatalog=""                                        \
  -Djdbcschema=USER1                                      \
  -Djdbcdrivergroupid=com.oracle.ojdbc                    \
  -Djdbcdriverartifactid=ojdbc8                           \
  -Djdbcdriverversion=19.3.0.0


Example with Oracle database:

mvn archetype:generate -DinteractiveMode=false            \
  -DarchetypeGroupId=org.hotrodorm.hotrod                 \
  -DarchetypeArtifactId=hotrod-archetype-sm-jar-app       \
  -DarchetypeVersion=3.4.5-SNAPSHOT                       \
  -DgroupId=com.app1                                      \
  -DartifactId=app1                                       \
  -Dversion=1.0.0-SNAPSHOT                                \
  -Dpackage=com.myapp                                     \
  -Dpersistencepackage=persistence                        \
  -Djdbcdriverclassname=oracle.jdbc.driver.OracleDriver   \
  -Djdbcurl="jdbc:oracle:thin:@192.168.56.95:1521:orcl"   \
  -Djdbcusername=user1                                    \
  -Djdbcpassword=pass1                                    \
  -Djdbccatalog=""                                        \
  -Djdbcschema=USER1                                      \
  -Djdbcdrivergroupid=com.oracle.ojdbc                    \
  -Djdbcdriverartifactid=ojdbc8                           \
  -Djdbcdriverversion=19.3.0.0


