package by.afinny.investments.mapper;

import by.afinny.investments.dto.RequestNewAccountAgreeDto;
import by.afinny.investments.dto.ResponseNewAccountAgreeDto;
import by.afinny.investments.dto.kafka.ProducerNewAccountAgree;
import by.afinny.investments.entity.AccountAgree;
import org.mapstruct.Mapper;

@Mapper
public interface AccountAgreeMapper {

    ResponseNewAccountAgreeDto toResponseNewAccountAgreeDto(RequestNewAccountAgreeDto requestNewAccountAgreeDto,
                                                            AccountAgree accountAgree);

    ProducerNewAccountAgree toProducerNewAccountAgree(ResponseNewAccountAgreeDto responseNewAccountAgreeDto);
}
