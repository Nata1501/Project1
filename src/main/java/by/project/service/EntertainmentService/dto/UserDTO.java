package by.project.service.EntertainmentService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDTO {
    @NotEmpty(message = "Login should not be empty")
    @Size(min=5, max=20, message = "Login should be between 5 and 20 characters")
    private String login;

    @Email
    private String email;

    private List<EventDTO> events;





    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

}
