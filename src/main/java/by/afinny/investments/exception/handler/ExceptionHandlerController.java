package by.afinny.investments.exception.handler;

import by.afinny.investments.exception.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> badRequestExceptionHandler(EntityNotFoundException e) {
        log.error("Bad request. " + e.getMessage());
        ErrorDto body = new ErrorDto(Integer.toString(HttpStatus.BAD_REQUEST.value()), e.getMessage());
        return createResponseEntity(HttpStatus.BAD_REQUEST, body);
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> serverExceptionHandler(Exception e) {
        log.error("Internal server error." + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<ErrorDto> createResponseEntity(HttpStatus status, ErrorDto errorDto) {
        return ResponseEntity.status(status)
                .header("Content-Type", "application/json")
                .body(errorDto);
    }
}
