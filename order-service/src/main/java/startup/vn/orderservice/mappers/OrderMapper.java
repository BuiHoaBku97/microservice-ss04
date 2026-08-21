package startup.vn.orderservice.mappers;

import org.springframework.stereotype.Component;
import startup.vn.orderservice.dtos.requests.OrderRequestDTO;
import startup.vn.orderservice.dtos.responses.OrderResponseDTO;
import startup.vn.orderservice.entities.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDTO requestDTO, BigDecimal totalAmount) {
        return new Order(
                null,
                requestDTO.getCustomerId(),
                requestDTO.getProductId(),
                LocalDateTime.now(),
                totalAmount
        );
    }

    public OrderResponseDTO toResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getProductId(),
                order.getOrderDate(),
                order.getTotalAmount()
        );
    }
}
