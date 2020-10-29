package jpa.spring.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:context.xml")
@ComponentScan(basePackages = { "jpa.spring.poc" })
public class JpaSpring4HotRod3Application {

	public static void main(String[] args) {
		SpringApplication.run(JpaSpring4HotRod3Application.class, args);
	}

}
