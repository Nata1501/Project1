package by.project.service.entertainment.models.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;



@Entity
@Table(name = "Event")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_entertainment", referencedColumnName = "id")
    private Entertainment entertainment;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "id_place", referencedColumnName = "id")
    private Place place;

    @Column(name = "date_time")
    @NotNull
    private LocalDateTime dateTime;

    @ManyToMany(mappedBy = "events")
    private List<User> users;

    @NotNull
    private Boolean relevance;
}
