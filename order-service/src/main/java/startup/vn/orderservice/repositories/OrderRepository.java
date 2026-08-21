package startup.vn.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startup.vn.orderservice.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
