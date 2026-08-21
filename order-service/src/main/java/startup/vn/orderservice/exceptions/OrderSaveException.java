package startup.vn.orderservice.exceptions;

public class OrderSaveException extends RuntimeException {
    public OrderSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
