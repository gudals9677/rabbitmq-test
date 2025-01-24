package rabbitMq_test.rabbitMq.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    // RabbitMQ에서 메시지를 수신하고 WebSocket으로 전달
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(ChatMessage message) {
        log.info("메세지 받은 후 수신자에게 발송 :: {}", message.toString());
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
