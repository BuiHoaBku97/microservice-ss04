package startup.vn.orderservice.clients.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import startup.vn.orderservice.exceptions.ProductServiceUnavailableException;
import startup.vn.orderservice.exceptions.ResourceNotFoundException;

@Component
public class ProductCatalogClient {

    private final RestTemplate restTemplate;
    private final String productServiceBaseUrl;

    public ProductCatalogClient(RestTemplate restTemplate,
                                @Value("${product.service.base-url}") String productServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.productServiceBaseUrl = productServiceBaseUrl;
    }

    public ProductResponseDTO getProductById(Long productId) {
        try {
            return restTemplate.getForObject(
                    productServiceBaseUrl + "/api/v1/products/{id}",
                    ProductResponseDTO.class,
                    productId
            );
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        } catch (HttpServerErrorException ex) {
            throw new ProductServiceUnavailableException("Product service is unavailable", ex);
        } catch (RestClientResponseException ex) {
            throw new ProductServiceUnavailableException("Product service is unavailable", ex);
        } catch (ResourceAccessException ex) {
            throw new ProductServiceUnavailableException("Product service is unavailable", ex);
        }
    }
}
