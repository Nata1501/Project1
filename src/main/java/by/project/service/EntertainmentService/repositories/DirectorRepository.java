package by.project.service.EntertainmentService.repositories;

import by.project.service.EntertainmentService.models.person.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    Optional<Director> findByName(String name);

}
