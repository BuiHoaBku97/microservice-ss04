package startup.vn.orderservice.exceptions;

public class ProductServiceUnavailableException extends RuntimeException {
    public ProductServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
