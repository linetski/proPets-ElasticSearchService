package propets.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class ElasticSearchServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchServiceApplication.class, args);
	}

	/*
	 * @EventListener(ApplicationReadyEvent.class) public void
	 * doSomethingAfterStartup() { String email = "linetski@gmail.com"; String id =
	 * "60a1b4be142d961998dbd4bc"; logger.info("FoundPet id = " + id);
	 * sendEmail("you pet found", "http://localhost:3000/main_page/found/card/"+id,
	 * email); }
	 */

}
