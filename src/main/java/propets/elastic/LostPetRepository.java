package propets.elastic;

import org.springframework.data.repository.CrudRepository;

import propets.model.LostPet;

public interface LostPetRepository extends CrudRepository<LostPet, String>{

}
