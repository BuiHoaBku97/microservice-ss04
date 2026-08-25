package startup.vn.orderservice.clients.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;
import startup.vn.orderservice.exceptions.ResourceNotFoundException;
import startup.vn.orderservice.exceptions.ProductServiceUnavailableException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.GET;

class ProductCatalogClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ProductCatalogClient productCatalogClient;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        productCatalogClient = new ProductCatalogClient(restTemplate, "http://product-service", false);
    }

    @Test
    void getProductByIdShouldReturnProductFromApi() {
        mockServer.expect(requestTo("http://product-service/api/v1/products/12"))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                          "id": 12,
                          "name": "Laptop",
                          "price": 125.50,
                          "stockQuantity": 10
                        }
                        """, MediaType.APPLICATION_JSON));

        var product = productCatalogClient.getProductById(12L);

        assertEquals(12, product.id());
        assertEquals("Laptop", product.name());
        assertEquals(0, product.price().compareTo(new java.math.BigDecimal("125.50")));
        assertEquals(10, product.stockQuantity());

        mockServer.verify();
    }

    @Test
    void getProductByIdShouldTranslateNotFound() {
        mockServer.expect(requestTo("http://product-service/api/v1/products/99"))
                .andExpect(method(GET))
                .andRespond(org.springframework.test.web.client.response.MockRestResponseCreators
                        .withStatus(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> productCatalogClient.getProductById(99L));

        mockServer.verify();
    }

    @Test
    void getProductByIdShouldTranslateConnectionFailure() {
        mockServer.expect(requestTo("http://product-service/api/v1/products/7"))
                .andExpect(method(GET))
                .andRespond(withException(new IOException("Connection refused")));

        assertThrows(ProductServiceUnavailableException.class, () -> productCatalogClient.getProductById(7L));

        mockServer.verify();
    }

    @Test
    void getProductByIdShouldReturnFallbackWhenEnabled() {
        productCatalogClient = new ProductCatalogClient(restTemplate, "http://product-service", true);
        mockServer.expect(requestTo("http://product-service/api/v1/products/8"))
                .andExpect(method(GET))
                .andRespond(withException(new IOException("Connection refused")));

        var product = productCatalogClient.getProductById(8L);

        assertEquals(8, product.id());
        assertEquals("Sản phẩm mặc định", product.name());
        assertEquals(0, product.price().compareTo(java.math.BigDecimal.ZERO));
        assertEquals(0, product.stockQuantity());

        mockServer.verify();
    }
}
