package startup.vn.orderservice.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import startup.vn.orderservice.clients.product.ProductCatalogClient;
import startup.vn.orderservice.dtos.requests.OrderRequestDTO;
import startup.vn.orderservice.dtos.responses.OrderResponseDTO;
import startup.vn.orderservice.entities.Order;
import startup.vn.orderservice.exceptions.OrderSaveException;
import startup.vn.orderservice.exceptions.ResourceNotFoundException;
import startup.vn.orderservice.mappers.OrderMapper;
import startup.vn.orderservice.repositories.OrderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductCatalogClient productCatalogClient;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderMapper orderMapper,
                            ProductCatalogClient productCatalogClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productCatalogClient = productCatalogClient;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        var product = productCatalogClient.getProductById(orderRequestDTO.getProductId());
        var unitPrice = product.price();
        var totalAmount = unitPrice.multiply(BigDecimal.valueOf(orderRequestDTO.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);

        var order = orderMapper.toEntity(orderRequestDTO, totalAmount);

        try {
            var savedOrder = orderRepository.save(order);
            return orderMapper.toResponseDTO(savedOrder);
        } catch (DataAccessException ex) {
            throw new OrderSaveException("Failed to save order", ex);
        }
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));
        return orderMapper.toResponseDTO(order);
    }
}
