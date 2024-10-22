package by.project.service.EntertainmentService.models.person;


import by.project.service.EntertainmentService.models.main.Entertainment;
import by.project.service.EntertainmentService.models.other.ObjectClass;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Director")
public class Director extends ObjectClass {
    @OneToMany(mappedBy = "director")
    private List<Entertainment> entertainments;




    public Director() {
    }

    public Director(int id, String name) {
        super(id, name);
    }

    public List<Entertainment> getMovies() {
        return entertainments;
    }

    public void setMovies(List<Entertainment> entertainments) {
        this.entertainments = entertainments;
    }

}
