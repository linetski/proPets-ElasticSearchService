package propets.elastic.ElasticDemo;


import java.util.ArrayList;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import propets.model.Email;
import propets.model.FoundPet;
import propets.model.LostPet;

@SpringBootApplication
public class ElasticSearchServiveApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiveApplication.class);
	
	private static final String EMAIL_TOPIC = "email";

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
	
	private static final String LOST_PET_TOPIC = "lostpet";
	private static final String FOUND_PET_TOPIC = "foundpet";
 
	
	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchServiveApplication.class, args);
	}
	
	
	@KafkaListener(topics = FOUND_PET_TOPIC, groupId = "foo")
	public void consumeFound(FoundPet foundPet) {
		logger.info(foundPet.toString());
		foundPetRepository.save(foundPet);
		ArrayList<LostPet> resultList = restControler.find(foundPet);
		if(resultList.size()>0) {
			String email = foundPet.getEmail();
			//String email = "linetski@gmail.com";
			String id = resultList.get(0).getId();
			logger.info("LostPet id = " + id);
			sendEmail("LostPet card", "http://localhost:3000/main_page/lost/card/"+id, email);
		}
	}
	
	@KafkaListener(topics = LOST_PET_TOPIC, groupId = "foo")
	public void consumeLost(LostPet lostPet) {
		logger.info(lostPet.toString());
		lostPetRepository.save(lostPet);
		ArrayList<FoundPet> resultList = restControler.find(lostPet);
		if(resultList.size()>0) {
			String email = lostPet.getEmail();
			//String email = "linetski@gmail.com";
			String id = resultList.get(0).getId();
			logger.info("FoundPet id = " + id);
			sendEmail("you pet found", "http://localhost:3000/main_page/found/card/"+id, email);
		}
			

	}

	private void sendEmail(String subject, String body, String emailAdress) {
	    Email email = new Email();
	    email.setSubject(subject);
	    email.setBody(body);
	    email.setEmailAdress(emailAdress);
	    emailKafkaTemplate.send(EMAIL_TOPIC,email);
	}
	
	/*
	 * @EventListener(ApplicationReadyEvent.class) public void
	 * doSomethingAfterStartup() { String email = "linetski@gmail.com"; String id =
	 * "60a1b4be142d961998dbd4bc"; logger.info("FoundPet id = " + id);
	 * sendEmail("you pet found", "http://localhost:3000/main_page/found/card/"+id,
	 * email); }
	 */

}