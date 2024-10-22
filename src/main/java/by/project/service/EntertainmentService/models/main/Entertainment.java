package by.project.service.EntertainmentService.models.main;

import by.project.service.EntertainmentService.models.other.GenreType;
import by.project.service.EntertainmentService.models.other.Type;

import by.project.service.EntertainmentService.models.person.Director;
import by.project.service.EntertainmentService.models.person.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "Entertainment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Entertainment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min=1, max=50, message = "Name should be between 1 and 50 characters")
    private String name;

    @Column(name = "description")
    @NotEmpty(message = "Description should not be empty")
    @Size(min=10, max=300, message = "Description should be between 10 and 300 characters")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Type type;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private GenreType genretype;  ////////////// String???????

    @Column(name = "duration")
    @Min(value=1, message = "Duration should be greater than 0 minutes")
    private double duration;

    @Column(name = "year")
    @Min(value=1895, message = "Year should be greater than 1895")
    @Max(value=2024, message = "Year should not be greater than 2024")
    private int year;

    @Column(name = "price")
    @Min(value=0, message = "Price should be zero or more rubles")
    private double price;

    @ManyToOne
    @JoinColumn(name = "id_director", referencedColumnName = "id")
    private Director director;

    @ManyToMany(mappedBy = "entertainments")
    private List<Person> performers;

    @OneToMany(mappedBy = "entertainment")
    private List<Event> events;

}
