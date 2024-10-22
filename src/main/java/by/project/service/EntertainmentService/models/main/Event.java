package by.project.service.EntertainmentService.models.main;


import by.project.service.EntertainmentService.models.other.Place;
import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.models.person.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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
    @Column(name = "id")
    private int id;

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
    private Date dateTime;

    @ManyToMany(mappedBy = "events")
    private List<User> users;

    @Column(name = "relevance")
    @NotNull
    private Boolean relevance;

}
