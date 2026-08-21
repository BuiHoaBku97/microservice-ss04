package startup.vn.customerservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import startup.vn.customerservice.dtos.requests.CustomerRequestDTO;
import startup.vn.customerservice.dtos.requests.LoginRequestDTO;
import startup.vn.customerservice.dtos.responses.CustomerResponseDTO;
import startup.vn.customerservice.entities.Customer;
import startup.vn.customerservice.exceptions.InvalidCredentialsException;
import startup.vn.customerservice.exceptions.ResourceNotFoundException;
import startup.vn.customerservice.exceptions.UniqueValueException;
import startup.vn.customerservice.mappers.CustomerMapper;
import startup.vn.customerservice.repositories.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.existsByEmail(customerRequestDTO.getEmail())){
            // Customer with this email already exists
            throw new UniqueValueException("Customer with email " + customerRequestDTO.getEmail() + " already exists");
        }

        var encryptedPassword = encryptPassword(customerRequestDTO.getPassword());
        var customer = customerMapper.toEntity(customerRequestDTO, encryptedPassword);

        var savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    private String encryptPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    @Override
    public CustomerResponseDTO getCustomerById(Integer id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO login(LoginRequestDTO loginRequestDTO) {
        var customer = customerRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("email or password incorrect"));

        var passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), customer.getPassword())) {
            throw new InvalidCredentialsException("email or password incorrect");
        }

        return customerMapper.toResponseDTO(customer);
    }
}
