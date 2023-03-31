package by.afinny.investments.integration;

import by.afinny.investments.dto.kafka.ProducerNewAccountAgree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewAccountAgreeSours {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.new-account-agree-producer.path}")
    private String kafkaTopic;

    @EventListener
    public void sendMessageAboutAccountAgreeInformation(ProducerNewAccountAgree producerNewAccountAgree) {
        log.info("Event " + producerNewAccountAgree + " has been received, sending message...");
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(producerNewAccountAgree)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(KafkaHeaders.TOPIC, kafkaTopic)
                        .build()
        );
    }
}
