package propets.elastic.Repositories;

import org.springframework.data.repository.CrudRepository;

import propets.model.LostPet;

public interface LostPetRepository extends CrudRepository<LostPet, String>{

}
