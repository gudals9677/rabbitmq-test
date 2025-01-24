package rabbitMq_test.rabbitMq.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

    @MessageMapping("/send") // 클라이언트가 보낸 메시지를 처리
    @SendTo("/topic/messages") // 모든 클라이언트로 브로드캐스트
    public ChatMessage broadcastMessage(ChatMessage message) {
        log.info("받은 메세지 확인 :: {}", message.toString());
        return message; // 받은 메시지를 그대로 반환
    }
}
