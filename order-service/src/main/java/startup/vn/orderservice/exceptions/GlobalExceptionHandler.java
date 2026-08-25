package startup.vn.orderservice.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import startup.vn.orderservice.dtos.responses.ApiResponseError;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.BAD_REQUEST.value()),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message
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

    @ExceptionHandler(OrderSaveException.class)
    public ResponseEntity<ApiResponseError> handleOrderSave(OrderSaveException ex) {
        log.error("Order save failed", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Internal Server Error"
                )
        );
    }

    @ExceptionHandler(ProductServiceUnavailableException.class)
    public ResponseEntity<ApiResponseError> handleProductServiceUnavailable(ProductServiceUnavailableException ex) {
        log.warn("Product Service unavailable: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                        HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponseError> handleDataAccess(DataAccessException ex) {
        log.error("Database access failed", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Internal Server Error"
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError> handleUnexpected(Exception ex) {
        log.error("Unexpected error in Order Service", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponseError(
                        LocalDateTime.now(),
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Internal Server Error"
                )
        );
    }
}
