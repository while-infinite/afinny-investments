package by.afinny.investments.integration;

import by.afinny.investments.dto.kafka.ConsumerNewAccountAgree;
import by.afinny.investments.service.AccountAgreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewAccountAgreeListener {

    private final AccountAgreeService accountAgreeService;

    @KafkaListener(
            topics = "${kafka.topics.new-account-agree-listener.path}",
            groupId = "investment-service",
            containerFactory = "kafkaListenerNewAccountAgree"
    )
    public void onRequestChangeAccountAgreeStatus(Message<ConsumerNewAccountAgree> message) {
        ConsumerNewAccountAgree consumerNewAccountAgree = message.getPayload();
        log.info("Change accountAgree.isActive to: {}", consumerNewAccountAgree.isApproved());
        accountAgreeService.updateStatus(consumerNewAccountAgree.getId(), consumerNewAccountAgree.isApproved());
    }
}
