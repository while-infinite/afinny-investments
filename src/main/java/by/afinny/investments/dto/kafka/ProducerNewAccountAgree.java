package by.afinny.investments.dto.kafka;

import by.afinny.investments.dto.ResponseNewAccountAgreeDto;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProducerNewAccountAgree {
    UUID id;
    private ResponseNewAccountAgreeDto responseNewAccountAgreeDto;
}
