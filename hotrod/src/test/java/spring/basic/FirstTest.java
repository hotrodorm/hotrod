package spring.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask;

public class FirstTest {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    HotRodAntTask at = new HotRodAntTask();
    Properties p = new Properties();
    String myCurrentDir = System.getProperty("user.dir").replace("\\", "/");
    System.out.println(myCurrentDir);
    // D:\alvaro\git\empusa_new\empusamybatis\empusamybatis\spring\basic
    // D:/alvaro/git/empusa_new/empusamybatis/empusamybatis
    p.load(new FileInputStream(myCurrentDir + "/conf/workbench.properties"));
    p.load(new FileInputStream(myCurrentDir + "/testdata/hsqldb/config.properties"));

    at.setGenerator(p.getProperty("generator"));
    at.setCatalog(p.getProperty("catalog"));
    at.setSchema(p.getProperty("schema"));
    at.setUrl(p.getProperty("url"));
    
    String configFile =myCurrentDir + "/" + p.getProperty("db.src.dir") + "/empusa-mybatis.xml"; 
    System.out.println(configFile);
//    File f = new File(configFile);
//    Reader reader = new BufferedReader(new FileReader(f));

    at.setConfigfile("D:/alvaro/git/empusa_new/empusamybatis/empusamybatis/testdata/hsqldb/empusa-mybatis.xml");
    at.setDriverclass(p.getProperty("driverclass"));
    at.setUsername(p.getProperty("username"));
    at.setPassword(p.getProperty("password"));
    at.setDisplay(p.getProperty("display"));

    URL url = new URL("file:///"+configFile);
     at.execute();
    // database=database
    // driverclasspath=lib/jdbc-drivers/hsqldb-2.2.9.jar
    // driverclass=org.hsqldb.jdbcDriver
    // #url=jdbc:hsqldb:mem:test;sql.syntax_ora=true
    // username=sa
    // password=
    // scenario=basic
    // <empusamb driverclass="${driverclass}"
    // url="${url}"
    // username="${username}"
    // password="${password}"
    // division="${division}"
    // generator="${generator}"
    // configfile="${db.src.dir}/empusa-mybatis.xml"
    // display="list" />
  }

}
