package startup.vn.orderservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import startup.vn.orderservice.clients.product.ProductCatalogClient;
import startup.vn.orderservice.clients.product.ProductResponseDTO;
import startup.vn.orderservice.dtos.requests.OrderRequestDTO;
import startup.vn.orderservice.entities.Order;
import startup.vn.orderservice.mappers.OrderMapper;
import startup.vn.orderservice.repositories.OrderRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductCatalogClient productCatalogClient;

    private final OrderMapper orderMapper = new OrderMapper();

    @Test
    void createOrderShouldUseProductPriceFromApi() {
        var orderService = new OrderServiceImpl(orderRepository, orderMapper, productCatalogClient);
        var request = new OrderRequestDTO(7L, 5L, 3);

        when(productCatalogClient.getProductById(5L))
                .thenReturn(new ProductResponseDTO(5, "Phone", new BigDecimal("199.99"), 20));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            var order = invocation.getArgument(0, Order.class);
            order.setId(1L);
            return order;
        });

        var response = orderService.createOrder(request);

        assertEquals(1L, response.id());
        assertEquals(7L, response.customerId());
        assertEquals(5L, response.productId());
        assertNotNull(response.orderDate());
        assertEquals(0, response.totalAmount().compareTo(new BigDecimal("599.97")));

        var orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(0, orderCaptor.getValue().getTotalAmount().compareTo(new BigDecimal("599.97")));
        verify(productCatalogClient).getProductById(5L);
    }

    @Test
    void getOrderByIdShouldReturnStoredOrder() {
        var orderService = new OrderServiceImpl(orderRepository, orderMapper, productCatalogClient);
        var order = new Order(2L, 7L, 5L, java.time.LocalDateTime.now(), new BigDecimal("599.97"));

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        var response = orderService.getOrderById(2L);

        assertEquals(2L, response.id());
        assertEquals(7L, response.customerId());
        assertEquals(5L, response.productId());
        assertEquals(0, response.totalAmount().compareTo(new BigDecimal("599.97")));
    }
}
