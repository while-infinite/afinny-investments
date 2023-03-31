package by.afinny.investments.service.impl;

import by.afinny.investments.dto.RequestNewAccountAgreeDto;
import by.afinny.investments.dto.ResponseNewAccountAgreeDto;
import by.afinny.investments.dto.kafka.ProducerNewAccountAgree;
import by.afinny.investments.entity.AccountAgree;
import by.afinny.investments.mapper.AccountAgreeMapper;
import by.afinny.investments.service.AccountAgreeService;
import by.afinny.investments.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final AccountAgreeService accountAgreeService;
    private final ApplicationEventPublisher eventPublisher;
    private final AccountAgreeMapper accountAgreeMapper;

    @Override
    @Transactional
    public ResponseNewAccountAgreeDto registerNewAccountAgree(RequestNewAccountAgreeDto requestNewAccountAgreeDto) {
        AccountAgree accountAgree = accountAgreeService.createAccountAgree();
        ResponseNewAccountAgreeDto responseNewAccountAgreeDto = accountAgreeMapper
                .toResponseNewAccountAgreeDto(requestNewAccountAgreeDto, accountAgree);

        ProducerNewAccountAgree producerNewAccountAgree = ProducerNewAccountAgree.builder()
                .id(accountAgree.getId())
                .responseNewAccountAgreeDto(responseNewAccountAgreeDto)
                .build();

        sendToKafka(producerNewAccountAgree);

        return responseNewAccountAgreeDto;
    }

    private void sendToKafka(ProducerNewAccountAgree producerNewAccountAgree) {
        log.info("Publishing event: ResponseNewAccountAgreeDto");

        eventPublisher.publishEvent(producerNewAccountAgree);
    }
}
