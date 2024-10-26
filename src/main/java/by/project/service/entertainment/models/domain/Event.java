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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Entertainment entertainment;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne
    private Place place;

    @NotNull
    private LocalDateTime dateTime;

    @ManyToMany(mappedBy = "events")
    private List<User> users;

    @NotNull
    private Boolean relevance;

}
