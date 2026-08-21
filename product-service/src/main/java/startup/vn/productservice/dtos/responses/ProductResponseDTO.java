package startup.vn.productservice.dtos.responses;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Integer id,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {
}
