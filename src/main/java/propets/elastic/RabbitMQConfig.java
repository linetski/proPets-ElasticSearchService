package propets.elastic;



import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	@Value("${rabbitmq.queue.email}")
	String queueName;

	@Value("${rabbitmq.exchange.email}")
	String exchange;

	@Value("${rabbitmq.routingkey.email}")
	private String routingkey;
	
	@Value("${rabbitmq.exchange.lostpet}")
	private String lostpetExchange;
	
	@Value("${rabbitmq.routingkey.lostpet}")
	private String lostpetRoutingkey;
	
	@Value("${rabbitmq.exchange.foundpet}")
	private String foundpetExchange;
	
	@Value("${rabbitmq.routingkey.foundpet}")
	private String foundpetRoutingkey;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	DirectExchange exchange() {
		DirectExchange directExchenge = new DirectExchange(exchange,false,false);
		return directExchenge;
	}

	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		BindingBuilder.bind(new Queue(lostpetRoutingkey, false)).to(new DirectExchange(lostpetExchange,false,false)).with(lostpetRoutingkey);
		BindingBuilder.bind(new Queue(foundpetRoutingkey, false)).to(new DirectExchange(foundpetExchange,false,false)).with(foundpetRoutingkey);
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;
	}

}
