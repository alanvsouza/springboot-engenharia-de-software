package br.unesp.rc.springtutorial;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import br.unesp.rc.springtutorial.entity.Fisica;
import br.unesp.rc.springtutorial.utils.InstanceGenerator;

@SpringBootApplication
@EnableCaching
public class SpringtutorialApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringtutorialApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Fisica entity = InstanceGenerator.getPessoaFisica("111.222.333-44", "user1");
		System.out.println("\n" + entity + "\n");
	}
}
