package propets.elastic.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import propets.model.FoundPet;
@Repository
public interface FoundPetRepository extends CrudRepository<FoundPet, String>{

	Optional<FoundPet> findById(String id);

}
