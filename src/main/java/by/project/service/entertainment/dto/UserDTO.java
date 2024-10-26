package by.project.service.entertainment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Login should not be empty")
    @Size(min=5, max=20, message = "Login should be between 5 and 20 characters")
    private String login;

    @Email
    private String email;

    private List<EventDTO> events;

}
