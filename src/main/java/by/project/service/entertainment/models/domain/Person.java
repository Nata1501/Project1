package by.project.service.entertainment.models.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Person extends ObjectClass {

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "person_entertainment",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "entertainment_id"))
    private List<Entertainment> entertainments;

}
