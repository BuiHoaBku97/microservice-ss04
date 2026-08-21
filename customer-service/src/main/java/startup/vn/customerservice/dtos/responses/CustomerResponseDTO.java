package startup.vn.customerservice.dtos.responses;

public record CustomerResponseDTO(
    int id,
    String fullName,
    String email
){}