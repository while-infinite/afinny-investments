package by.afinny.investments.dto;

import lombok.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestNewAccountAgreeDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String clientId;
    private String email;
    private String passportNumber;
}
