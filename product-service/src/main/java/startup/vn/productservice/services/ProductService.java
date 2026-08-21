package startup.vn.productservice.services;

import startup.vn.productservice.dtos.requests.ProductRequestDTO;
import startup.vn.productservice.dtos.responses.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO getProductById(Integer id);
    List<ProductResponseDTO> getAllProducts();
}
