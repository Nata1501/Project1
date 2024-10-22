package by.project.service.EntertainmentService.repositories;

import by.project.service.EntertainmentService.models.other.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByName(String name);

}
