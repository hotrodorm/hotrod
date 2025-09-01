package hr3.springboot.poc;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackageClasses = HotRod3PocApplication.class)

@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)

@ImportResource({"classpath:context.xml"})
public class HotRod3PocApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotRod3PocApplication.class, args);
	}

}
