package by.afinny.investments.integration.stub;

import by.afinny.investments.dto.kafka.ConsumerNewAccountAgree;
import by.afinny.investments.dto.kafka.ProducerNewAccountAgree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class StubNewAccountAgreeABC {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.new-account-agree-listener.path}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topics.new-account-agree-producer.path}",
            groupId = "investment-service",
            containerFactory = "stubKafkaListenerNewAccountAgree"
    )
    public void receiveProducerAndSendConsumerNewAccountAgreeEvent(Message<ProducerNewAccountAgree> message) {
        log.info("receiveProducerAndSendConsumerNewAccountAgreeEvent() method invoked");

        ProducerNewAccountAgree producerNewAccountAgree = message.getPayload();
        ConsumerNewAccountAgree consumerNewAccountAgree = setUpEvent(producerNewAccountAgree);

        sendEvent(consumerNewAccountAgree);
    }

    private ConsumerNewAccountAgree setUpEvent(ProducerNewAccountAgree producerNewAccountAgree) {
        log.debug("setUpEvent() method invoked");

        return ConsumerNewAccountAgree.builder()
                .id(producerNewAccountAgree.getId())
                .approved(true)
                .build();
    }

    private void sendEvent(ConsumerNewAccountAgree event) {
        log.debug("sendEvent() method invoked");
        log.debug("send event: " + event);

        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(event)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build());
    }
}
