package by.project.service.EntertainmentService.models.other;


import by.project.service.EntertainmentService.models.main.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Place")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Place extends ObjectClass {
    @Column(name = "capacity")
    @Min(value=1, message = "Capacity should be greater than 0")
    private int capacity;

    @OneToMany(mappedBy = "place")
    private List<Event> events;

}
