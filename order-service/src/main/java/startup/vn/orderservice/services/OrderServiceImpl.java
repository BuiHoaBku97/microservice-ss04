package startup.vn.orderservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        var unitPrice = resolveSimulatedUnitPrice(orderRequestDTO.getProductId());
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

    private BigDecimal resolveSimulatedUnitPrice(Long productId) {
        return switch (Math.floorMod(productId.intValue(), 5)) {
            case 0 -> BigDecimal.valueOf(100);
            case 1 -> BigDecimal.valueOf(150);
            case 2 -> BigDecimal.valueOf(200);
            case 3 -> BigDecimal.valueOf(250);
            default -> BigDecimal.valueOf(300);
        };
    }
}
