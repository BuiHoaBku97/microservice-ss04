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

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProductCatalogClient {

    private static final Logger log = LoggerFactory.getLogger(ProductCatalogClient.class);

    private final RestTemplate restTemplate;
    private final String productServiceBaseUrl;
    private final boolean fallbackEnabled;

    public ProductCatalogClient(RestTemplate restTemplate,
                                @Value("${product.service.base-url}") String productServiceBaseUrl,
                                @Value("${product.service.fallback.enabled:false}") boolean fallbackEnabled) {
        this.restTemplate = restTemplate;
        this.productServiceBaseUrl = productServiceBaseUrl;
        this.fallbackEnabled = fallbackEnabled;
    }

    public ProductResponseDTO getProductById(Long productId) {
        try {
            var product = restTemplate.getForObject(
                    productServiceBaseUrl + "/api/v1/products/{id}",
                    ProductResponseDTO.class,
                    productId
            );
            if (product == null) {
                throw new ProductServiceUnavailableException(
                        "Dịch vụ sản phẩm hiện không khả dụng, vui lòng thử lại sau",
                        null
                );
            }
            return product;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Sản phẩm với id " + productId + " không tồn tại");
        } catch (HttpServerErrorException ex) {
            return handleUnavailable(productId, ex);
        } catch (RestClientResponseException ex) {
            return handleUnavailable(productId, ex);
        } catch (ResourceAccessException ex) {
            return handleUnavailable(productId, ex);
        }
    }

    private ProductResponseDTO handleUnavailable(Long productId, Exception ex) {
        log.warn("Unable to reach Product Service for productId={}: {}", productId, ex.getMessage());

        if (fallbackEnabled) {
            return fallbackProduct(productId);
        }

        throw new ProductServiceUnavailableException(
                "Dịch vụ sản phẩm hiện không khả dụng, vui lòng thử lại sau",
                ex
        );
    }

    private ProductResponseDTO fallbackProduct(Long productId) {
        return new ProductResponseDTO(
                Math.toIntExact(productId),
                "Sản phẩm mặc định",
                BigDecimal.ZERO,
                0
        );
    }
}
