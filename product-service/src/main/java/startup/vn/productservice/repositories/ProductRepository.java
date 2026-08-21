package startup.vn.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startup.vn.productservice.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
