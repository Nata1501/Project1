package by.project.service.entertainment.models.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Entertainment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 50, message = "Name should be between 1 and 50 characters")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(min = 10, max = 300, message = "Description should be between 10 and 300 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private GenreType genretype;  ////////////// String???????

    @Min(value = 60, message = "Duration should be greater than 0 minutes")
    private Duration duration;

    @PastOrPresent
    private LocalDateTime dateTime;

    @PositiveOrZero(message = "Price should be zero or more rubles")
    private BigDecimal price;

    @ManyToOne
    private Director director;

    @ManyToMany(mappedBy = "entertainments")
    private List<Person> performers;

    @OneToMany(mappedBy = "entertainment")
    private List<Event> events;

}
