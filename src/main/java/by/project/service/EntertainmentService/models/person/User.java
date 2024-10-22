package by.project.service.EntertainmentService.models.person;


import by.project.service.EntertainmentService.models.main.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login")
    @NotEmpty(message = "Login should not be empty")
    @Size(min=5, max=20, message = "Login should be between 5 and 20 characters")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    @Size(min=6, max=20, message = "Password should be between 6 and 20 characters")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "card_balance")
    @NotNull
    @PositiveOrZero
    private Double cardBalance;

    @ManyToMany
    @JoinTable(
            name = "User_Event",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns =  @JoinColumn(name = "id_event"))
    private List<Event> events;






    public User() {
    }

    public User(int id, String login, String password, String email, Double cardBalance, List<Event> events) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.cardBalance = cardBalance;
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword()  {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(Double cardBalance) {
        this.cardBalance = cardBalance;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
