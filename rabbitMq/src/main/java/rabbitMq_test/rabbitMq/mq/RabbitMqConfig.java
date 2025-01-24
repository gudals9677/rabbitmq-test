package rabbitMq_test.rabbitMq.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitmqPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Queue는 RabbitMQ에서 메시지를 받을 수 있는 "큐"를 의미
     * 이 메서드는 큐 이름을 바탕으로 큐를 생성하여, 해당 큐로 메시지가 전송되도록 설정
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    /**
     * Exchange는 메시지를 전송할 대상 큐를 결정하는 역할을 합니다
     * DirectExchange는 메시지가 정확한 RoutingKey에 해당하는 큐로 전달되는 방식입니다
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    /**
     * Queue와 Exchange를 연결하고, 라우팅 키(routingKey)를 사용하여 바인딩합니다
     * 이 메서드는 메시지가 특정 라우팅 키에 맞는 큐로 전달될 수 있도록 설정하는 역할을 합니다
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * ConnectionFactory 객체를 생성하여 RabbitMQ와의 연결을 설정합니다
     * 반환된 ConnectionFactory는 이후 RabbitTemplate에서 사용되어 실제 메시지를 전송합니다
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitmqHost);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    /**
     * RabbitTemplate 객체를 생성하여 RabbitMQ와 상호작용할 수 있도록 설정합니다
     * RabbitTemplate은 메시지를 보내거나 받을 때 사용됩니다
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // JSON 형식의 메시지를 직렬화하고 역직렬할 수 있도록 설정
        log.info("rabbitTemplate == {}", rabbitTemplate);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Jackson 라이브러리를 사용하여 메시지를 JSON 형식으로 변환하는 MessageConverter 빈을 생성
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

