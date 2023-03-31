package by.afinny.investments.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseNewAccountAgreeDto {
    private LocalDate agreeDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String clientId;
    private String email;
    private String passportNumber;
}
