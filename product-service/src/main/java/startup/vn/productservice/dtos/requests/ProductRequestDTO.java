package startup.vn.productservice.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "name must not be blank")
    private String name;

    @Min(value = 1, message = "price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "stockQuantity must not be negative")
    private Integer stockQuantity;
}
