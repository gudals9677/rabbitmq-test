package rabbitMq_test.rabbitMq.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {
    private String sender;
    private String content;
}
