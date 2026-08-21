package startup.vn.customerservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startup.vn.customerservice.dtos.requests.CustomerRequestDTO;
import startup.vn.customerservice.dtos.requests.LoginRequestDTO;
import startup.vn.customerservice.dtos.responses.CustomerResponseDTO;
import startup.vn.customerservice.services.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> registerCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        return ResponseEntity.ok(customerService.registerCustomer(customerRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/login")
    public ResponseEntity<CustomerResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(customerService.login(loginRequestDTO));
    }
}
