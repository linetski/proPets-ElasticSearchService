package propets.elastic.ElasticDemo;

import org.springframework.data.repository.CrudRepository;

import propets.model.LostPet;

public interface LostPetRepository extends CrudRepository<LostPet, String>{

}
