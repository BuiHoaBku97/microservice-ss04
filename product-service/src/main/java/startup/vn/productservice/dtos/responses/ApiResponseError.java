package startup.vn.productservice.dtos.responses;

import java.time.LocalDateTime;

public record ApiResponseError(
        LocalDateTime timestamp,
        String status,
        String error,
        String message
) {
}
