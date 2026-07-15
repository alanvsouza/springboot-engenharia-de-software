package br.unesp.rc.springtutorialmongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.unesp.rc.springtutorialmongodb.entity.Fisica;

public interface FisicaRepository extends MongoRepository<Fisica, String> {
    Fisica findByCpf(String cpf);
    void deleteByCpf(String cpf);
}
