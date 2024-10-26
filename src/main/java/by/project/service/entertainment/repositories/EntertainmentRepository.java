package by.project.service.entertainment.repositories;

import by.project.service.entertainment.models.domain.Entertainment;
import by.project.service.entertainment.models.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {

    List<Entertainment> findByType(Type type);
    Optional<Entertainment> findByTypeAndId(Type type, Long id);
    Optional<Entertainment> findByNameAndTypeAndDateTime(String name, Type type, LocalDateTime year);

}
