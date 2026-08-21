package startup.vn.customerservice.mappers;

import org.springframework.stereotype.Component;
import startup.vn.customerservice.dtos.requests.CustomerRequestDTO;
import startup.vn.customerservice.dtos.responses.CustomerResponseDTO;
import startup.vn.customerservice.entities.Customer;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO requestDTO, String encryptedPassword) {
        return new Customer(
                0,
                requestDTO.getFullName(),
                requestDTO.getEmail(),
                encryptedPassword
        );
    }

    public CustomerResponseDTO toResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail()
        );
    }
}
