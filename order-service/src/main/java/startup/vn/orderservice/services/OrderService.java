package startup.vn.orderservice.services;

import startup.vn.orderservice.dtos.requests.OrderRequestDTO;
import startup.vn.orderservice.dtos.responses.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO getOrderById(Long id);
}
