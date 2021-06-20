package propets.elastic.RestClient;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {
	
	@Value("${elasticUrl}")
	private String elasticUrl;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()  
            .connectedTo(elasticUrl).build();

        return RestClients.create(clientConfiguration).rest();                         
    }
    
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}

// ...

/*
 * @Autowired RestHighLevelClient highLevelClient;
 * 
 * RestClient lowLevelClient = highLevelClient.lowLevelClient();
 * 
 * // ...
 * 
 * IndexRequest request = new IndexRequest("spring-data", "elasticsearch",
 * randomID()) .source(singletonMap("feature", "high-level-rest-client"))
 * .setRefreshPolicy(IMMEDIATE);
 * 
 * IndexResponse response = highLevelClient.index(request);
 */