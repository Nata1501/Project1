package by.project.service.EntertainmentService.repositories;


import by.project.service.EntertainmentService.models.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByName(String name);

}
