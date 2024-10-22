package by.project.service.EntertainmentService.models.person;


import by.project.service.EntertainmentService.models.main.Entertainment;
import by.project.service.EntertainmentService.models.other.ObjectClass;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "Person")
public class Person extends ObjectClass {
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "Person_Entertainment",
        joinColumns = @JoinColumn(name = "id_person"),
        inverseJoinColumns = @JoinColumn(name = "id_entertainment"))
    private List<Entertainment> entertainments;





    public Person() {
    }

    public Person(int id, String name) {
        super(id, name);
    }

    public List<Entertainment> getEntertainments() {
        return entertainments;
    }

    public void setEntertainments(List<Entertainment> entertainments) {
        this.entertainments = entertainments;
    }

}
