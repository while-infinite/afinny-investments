package by.afinny.investments.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorDto {

    private final String errorCode;
    private final String errorMessage;
}
