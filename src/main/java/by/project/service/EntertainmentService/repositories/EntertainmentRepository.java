package by.project.service.EntertainmentService.repositories;

import by.project.service.EntertainmentService.models.main.Entertainment;
import by.project.service.EntertainmentService.models.other.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Integer> {

    List<Entertainment> findByType(Type type);
    Optional<Entertainment> findByTypeAndId(Type type, int id);
    Optional<Entertainment> findByName(String name);
    Optional<Entertainment> findByNameAndTypeAndYear(String name, Type type, int year);

}
