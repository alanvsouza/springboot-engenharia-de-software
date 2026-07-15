package br.unesp.rc.springtutorial.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.unesp.rc.springtutorial.entity.Endereco;
import br.unesp.rc.springtutorial.entity.Fisica;
import br.unesp.rc.springtutorial.utils.InstanceGenerator;

@SpringBootTest
public class FisicaServiceTest {
    private Fisica entity;

    @Autowired
    private FisicaService fs = new FisicaService();

    // @Disabled
    @Test
    @DisplayName("FisicaService.save(Fisica)")
    void testSave() {
        entity = InstanceGenerator.getPessoaFisica("222.333.444-55", "user1");
        System.out.println(entity);

        Fisica f = fs.save(entity);
        System.out.println("----------------------------------------");
        System.out.println(f);
        System.out.println("----------------------------------------");

        assertEquals(entity, f);
    }

    @Disabled
    @Test
    @DisplayName("FisicaService.findByCpf(cpf)")
    void testFindByCpf() {
        entity = InstanceGenerator.getPessoaFisica("222.333.444-55", "user1");

        String cpf = "222.333.444-55";
        Fisica f = fs.findByCpf(cpf);
        System.out.println("----------------------------------------");
        System.out.println("Resultado do findByCpf:");
        System.out.println("----------------------------------------");
        System.out.println(f);
        System.out.println("----------------------------------------");

        assertEquals(entity, f);
    }

    @Test
    void testDelete() {
        assertDoesNotThrow(() -> {
            entity = fs.findByCpf("333.444.555-66");
            fs.delete(entity);
            System.out.println("Deletado!");
        });
    }

    @Test
    void testUpdate() {
        String cpfTeste = "333.444.555-66";

        Fisica jaExistente = fs.findByCpf(cpfTeste);
        if (jaExistente != null) {
            fs.delete(jaExistente);
        }

        Fisica fisicaParaSalvar = InstanceGenerator.getPessoaFisica(cpfTeste, "user2");
        Fisica fisicaSalva = fs.save(fisicaParaSalvar);

        Endereco e = new Endereco();
        e.setRua("Avenida 24A");
        e.setNumero(4040);
        e.setBairro("Bela Vista");
        e.setCep("13506-900");
        e.setCidade("Rio Claro");
        e.setEstado("SP");

        fisicaSalva.setEndereco(e);

        Fisica fisicaAtualizada = fs.update(fisicaSalva);

        assertNotNull(fisicaAtualizada, "A entidade atualizada não deveria ser nula");
        
        Fisica fisicaNoBanco = fs.findByCpf(cpfTeste);
        
        assertNotNull(fisicaNoBanco, "A entidade atualizada deveria ser encontrada no banco");
        assertEquals(fisicaSalva.getEndereco().size(), fisicaNoBanco.getEndereco().size(), "A lista de endereços deveria conter exatamente 1 endereço a mais que inicialmente");
    }

    @Disabled
    @Test
    @DisplayName("FisicaService.findAll()")
    void testFindAll() {
        System.out.println("findAll");

        List<Fisica> expResult = null;
        System.out.println("----------------------------------------");
        System.out.println("Resultado do findAll:");
        System.out.println("----------------------------------------");
        List<Fisica> result = fs.findAll();
        for (Fisica f : result) {
            System.out.println("----------------------------------------");
            System.out.println("FISICA: " + f);
            System.out.println("----------------------------------------");
        }
        assertNotEquals(expResult, result);
    }
}
