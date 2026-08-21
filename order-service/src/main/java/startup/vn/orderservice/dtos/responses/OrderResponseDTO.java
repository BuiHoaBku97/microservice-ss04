package startup.vn.orderservice.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponseDTO(
        Long id,
        Long customerId,
        Long productId,
        LocalDateTime orderDate,
        BigDecimal totalAmount
) {
}
