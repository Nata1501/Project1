package by.project.service.entertainment.models.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Place extends ObjectClass {

    @Min(value=1, message = "Capacity should be greater than 0")
    private Integer capacity;

    @OneToMany(mappedBy = "place")
    private List<Event> events;

}
