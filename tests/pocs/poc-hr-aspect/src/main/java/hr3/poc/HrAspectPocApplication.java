package hr3.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import hr3.poc.dao.action.NewsImpl;
import hr3.poc.dao.action.primitives.ActionDAO;
import hr3.poc.dao.action.primitives.NewsDAO;

@SpringBootApplication
@ImportResource({ "context.xml" })
public class HrAspectPocApplication implements CommandLineRunner {
	@Autowired
	ActionDAO adao;
	@Autowired
	NewsDAO ndao;

	public static void main(String[] args) {
		SpringApplication.run(HrAspectPocApplication.class, args);

	}

	public void run(String... args) throws Exception {
		NewsImpl n = ndao.selectByPK(1L);
		System.out.println("*" + n.getNewsContent());
		// From superclass (load superclass)
		System.out.println("*" + n.getTitle());
		// From superclass (do not load superclass, reuse already-loaded one)
		System.out.println("*" + n.getTitle());
		n.setActionId(null);

	}
}
