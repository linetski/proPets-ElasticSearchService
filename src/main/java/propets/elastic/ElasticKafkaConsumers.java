package propets.elastic;

import java.util.ArrayList;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import propets.model.Email;
import propets.model.FoundPet;
import propets.model.LostPet;

@Component
@RefreshScope
public class ElasticKafkaConsumers {
	
	private static final Logger logger = LoggerFactory.getLogger(ElasticKafkaConsumers.class);
	
	@Value("${emailTopic}")
	private String emailTopic;
	
	@Autowired 
	RestHighLevelClient highLevelClient;
	
	@Autowired
	FoundPetRepository foundPetRepository;
	
	@Autowired
	LostPetRepository lostPetRepository;
	
	@Autowired
	RestControler restControler;

	@Autowired
    private KafkaTemplate<String, Email> emailKafkaTemplate;
	
	@Autowired
	ElasticSearchService elasticSearchService;
	
	
	
	@KafkaListener(topics = "${foundPetTopic}", groupId = "foo")
	public void consumeFound(FoundPet foundPet) {
		logger.info(foundPet.toString());
		foundPetRepository.save(foundPet);
		ArrayList<LostPet> resultList = elasticSearchService.find(foundPet);
		if(resultList.size()>0) {
			//String email = foundPet.getEmail();
			String email = "olhali4265@gmail.com";
			String id = resultList.get(0).getId();
			logger.info("LostPet id = " + id);
			StringBuilder sb = new StringBuilder();
			sb.append("Hey!\n"
					+ "We received information about you found a pet, and we found a similar one among the lost.\n"
					+ "Follow this link: \n");
			sb.append("http://localhost:3000/main_page/lost/card/"+id);
			sb.append("\nBest regards, site \"ProPets\""
					+ "\n\n\n"
					+ "___\n"
					+ "This is an automatic letter.\n"
					+ "Please don't answer.");
			sendEmail("We received information about you found a pet",sb.toString(), email);
		}
	}
	
	@KafkaListener(topics = "${lostPetTopic}", groupId = "foo")
	public void consumeLost(LostPet lostPet) {
		logger.info(lostPet.toString());
		lostPetRepository.save(lostPet);
		ArrayList<FoundPet> resultList = elasticSearchService.find(lostPet);
		if(resultList.size()>0) {
			//String email = lostPet.getEmail();
			String email = "olhali4265@gmail.com";
			String id = resultList.get(0).getId();
			logger.info("FoundPet id = " + id);
			StringBuilder sb = new StringBuilder();
			sb.append("Hello!\n"
					+ "We received information about your lost pet and found a similar one among those found.\n"
					+ "Maybe you are looking for it: \n");
			sb.append("http://localhost:3000/main_page/found/card/"+id);
			sb.append("\nBest regards, site \"ProPets\"\n"
					+ "\n"
					+ "_\n"
					+ "This is an automatic letter.\n"
					+ "Please, don't answer.");
			sendEmail("We received information about your lost pet",sb.toString() , email);
		}
			

	}
	private void sendEmail(String subject, String body, String emailAdress) {
	    Email email = new Email();
	    email.setSubject(subject);
	    email.setBody(body);
	    email.setEmailAdress(emailAdress);
	    emailKafkaTemplate.send(emailTopic,email);
	}
}
