package startup.vn.orderservice.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleProductServiceUnavailableShouldReturnStructured503Response() {
        var response = handler.handleProductServiceUnavailable(
                new ProductServiceUnavailableException(
                        "Dich vu san pham hien khong kha dung, vui long thu lai sau",
                        new RuntimeException("connection refused")
                )
        );

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("503", response.getBody().status());
        assertEquals("Service Unavailable", response.getBody().error());
        assertEquals("Dich vu san pham hien khong kha dung, vui long thu lai sau", response.getBody().message());
    }

    @Test
    void handleResourceNotFoundShouldReturnStructured404Response() {
        var response = handler.handleResourceNotFound(new ResourceNotFoundException("Product not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("404", response.getBody().status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("Product not found", response.getBody().message());
    }
}
