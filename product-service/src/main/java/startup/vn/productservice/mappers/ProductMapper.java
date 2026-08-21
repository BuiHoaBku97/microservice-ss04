package startup.vn.productservice.mappers;

import org.springframework.stereotype.Component;
import startup.vn.productservice.dtos.requests.ProductRequestDTO;
import startup.vn.productservice.dtos.responses.ProductResponseDTO;
import startup.vn.productservice.entities.Product;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO requestDTO) {
        return new Product(
                null,
                requestDTO.getName(),
                requestDTO.getPrice(),
                requestDTO.getStockQuantity()
        );
    }

    public ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}
