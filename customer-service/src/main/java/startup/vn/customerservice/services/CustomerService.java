package startup.vn.customerservice.services;

import startup.vn.customerservice.dtos.requests.CustomerRequestDTO;
import startup.vn.customerservice.dtos.requests.LoginRequestDTO;
import startup.vn.customerservice.dtos.responses.CustomerResponseDTO;

public interface CustomerService {
    CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO getCustomerById(Integer id);
    CustomerResponseDTO login(LoginRequestDTO  loginRequestDTO);
}
