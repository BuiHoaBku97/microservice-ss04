package startup.vn.customerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startup.vn.customerservice.dtos.responses.ApiResponseError;

import java.time.LocalDateTime;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.NOT_FOUND.value()),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(UniqueValueException.class)
    public ResponseEntity<ApiResponseError> handleUniqueValue(UniqueValueException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.CONFLICT.value()),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponseError> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.BAD_REQUEST.value()),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Invalid value for parameter '" + ex.getName() + "'"
                )
        );
    }
}
