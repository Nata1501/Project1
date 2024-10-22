package by.project.service.EntertainmentService.repositories;

import by.project.service.EntertainmentService.models.main.Event;
import by.project.service.EntertainmentService.models.other.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByTypeAndRelevance(Type type, boolean relevance);
    List<Event> findByRelevance(boolean relevance);

}
