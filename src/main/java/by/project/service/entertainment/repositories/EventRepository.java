package by.project.service.entertainment.repositories;

import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTypeAndRelevance(Type type, boolean relevance);
    List<Event> findByRelevance(boolean relevance);

}
