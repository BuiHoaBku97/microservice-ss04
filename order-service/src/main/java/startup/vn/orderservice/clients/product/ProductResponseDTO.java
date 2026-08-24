package startup.vn.orderservice.clients.product;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Integer id,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {
}
