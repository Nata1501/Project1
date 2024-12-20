package by.project.service.entertainment.repositories;

import by.project.service.entertainment.models.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByName(String name);

}
