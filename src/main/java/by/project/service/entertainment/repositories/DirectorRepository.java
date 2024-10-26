package by.project.service.entertainment.repositories;

import by.project.service.entertainment.models.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    Optional<Director> findByName(String name);

}
