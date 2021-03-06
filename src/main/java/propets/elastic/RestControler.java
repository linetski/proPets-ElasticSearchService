package propets.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RefreshScope
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RestControler {
	

	@Autowired	
	ElasticSearchService elasticSearchService;
	
	@Autowired
	FoundPetRepository foundPetRepository;
	
	@Autowired
	LostPetRepository lostPetRepository;
	
	@Value("${user.role}")
	private String role;
	
	@PostMapping("/produce")
	public String produce() {
		//elasticSearchService.find();		
		return "";
	}
	
	@PostMapping("/clearLost")
	public String clearAllLost() {
		lostPetRepository.deleteAll();		
		return "";
	}
	
	@PostMapping("/clearFound")
	public String clearAlFound() {
		foundPetRepository.deleteAll();		
		return "";
	}

	@GetMapping("/clearFoundById/{id}")
	public String clearFoundById(@PathVariable String id) {
		foundPetRepository.deleteById(id);		
		return "";
	}
	
	@GetMapping("/clearLostById/{id}")
	public String clearLostById(@PathVariable String id) {
		lostPetRepository.deleteById(id);		
		return "";
	}
	
	@GetMapping("/test")
	public String test() {	
		return "test succesfully " + role;
	}
	
	
	
	
	

}
