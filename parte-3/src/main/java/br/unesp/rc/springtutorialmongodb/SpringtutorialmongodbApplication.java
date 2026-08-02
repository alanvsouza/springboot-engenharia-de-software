package br.unesp.rc.springtutorialmongodb;

import java.io.Console;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.unesp.rc.springtutorialmongodb.entity.Fisica;
import br.unesp.rc.springtutorialmongodb.service.FisicaService;
import br.unesp.rc.springtutorialmongodb.utils.InstanceGenerator;

@SpringBootApplication
public class SpringtutorialmongodbApplication implements CommandLineRunner {
	@Autowired
	private FisicaService fs = new FisicaService();

	public static void main(String[] args) {
		SpringApplication.run(SpringtutorialmongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		int op = -1;

		while (op != 6) {
			op = leitura();

			switch (op) {
				case 1: {
					System.out.println("Inserindo....");
					Fisica entity1 =
							InstanceGenerator.getPessoaFisica("111.222.333-44",
							"user1");
					Fisica entity2 =
							InstanceGenerator.getPessoaFisica("111.222.333-45",
							"user2");
					Fisica entity3 =
							InstanceGenerator.getPessoaFisica("111.222.333-46",
							"user3");

					fs.save(entity1);
					fs.save(entity2);
					fs.save(entity3);
					System.out.println("FIM INSERT\n");
				}
				break;
				case 2: {
					System.out.println("Atualizando....");
					final String cpf = "111.222.333-45";
					System.out.println("Localizando uma pessoa para o CPF: "
							+ cpf);
					Fisica fc = fs.findByCpf(cpf);
					System.out.println(fc);
					fc.setNome("ZÉ UPDATE DA SILVA");

					Fisica fu = fs.update(fc);
					System.out.println("---");
					System.out.println("Resultado UPDATE:");
					System.out.println("---");
					System.out.println(fu);
					System.out.println("---");
					System.out.println("FIM UPDATE\n");
				}
				break;
				case 3: {
					System.out.println("Excluindo....");
					final String cpf = "111.222.333-44";
					System.out.println("Localizando uma pessoa para o CPF: "
							+ cpf);
					Fisica fc = fs.findByCpf(cpf);
					System.out.println(fc);
					System.out.println("Excluindo a pessoa de CPF: "
							+ fc.getCpf());
					fs.delete(fc);
					System.out.println("FIM DELETE\n");
				}
				break;
				case 4: {
					System.out.println("Consultando....");
					final String cpf = "111.222.333-45";
					System.out.println("Localizando uma pessoa para o CPF: "
							+ cpf);
					Fisica fc = fs.findByCpf(cpf);
					System.out.println("---");
					System.out.println(fc);
					System.out.println("---");
					System.out.println("FIM SELECT\n");
				}
				break;
				case 5: {
					System.out.println("Listando todos....");
					List<Fisica> list = fs.findAll();
					for (Fisica f : list) {
						System.out.println(f);
						System.out.println("---");
					}
					System.out.println("FIM SELECT ALL");
				}
				break;
				case 6: {
					System.out.println("Saindo....");
					op = 6;
				}
				break;
			}
		}
	}

	private int leitura() {
		Console console = System.console();

		System.out.println("1 - Inserir");
		System.out.println("2 - Atualizar");
		System.out.println("3 - Excluir");
		System.out.println("4 - Consultar");
		System.out.println("5 - Listar todos");
		System.out.println("6 - Sair");
		System.out.print("OPÇÃO: ");
		int option = Integer.parseInt(console.readLine());

		return option;
	}
}
