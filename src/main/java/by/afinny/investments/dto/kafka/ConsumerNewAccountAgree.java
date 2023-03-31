package by.afinny.investments.dto.kafka;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConsumerNewAccountAgree {
    UUID id;
    private boolean approved;
}
