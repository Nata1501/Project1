package by.project.service.entertainment.models.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Login should not be empty")
    @Size(min=5, max=20, message = "Login should be between 5 and 20 characters")
    private String login;

    @NotEmpty(message = "Password should not be empty")
    @Size(min=6, max=20, message = "Password should be between 6 and 20 characters")
    private String password;

    @Email
    private String email;

    @NotNull
    @PositiveOrZero
    private BigDecimal cardBalance;

    @ManyToMany
    @JoinTable(
            name = "User_Event",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns =  @JoinColumn(name = "id_event"))
    private List<Event> events;

}
