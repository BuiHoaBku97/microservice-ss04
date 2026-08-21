package startup.vn.customerservice.exceptions;

public class UniqueValueException extends RuntimeException {
    public UniqueValueException(String message) {
        super(message);
    }
}
