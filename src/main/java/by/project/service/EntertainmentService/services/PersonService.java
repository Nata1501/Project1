package by.project.service.EntertainmentService.services;


import by.project.service.EntertainmentService.models.person.Director;
import by.project.service.EntertainmentService.models.person.Person;
import by.project.service.EntertainmentService.repositories.DirectorRepository;
import by.project.service.EntertainmentService.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final DirectorRepository directorRepository;
    private final PersonRepository personRepository;



    public Director findDirectorByName(String name) {
        return directorRepository.findByName(name).orElse(null);
      //  return director.orElse(null);
    }

    public Person findPersonByName(String name) {
        Optional<Person> person = personRepository.findByName(name);
        return person.orElse(null);
    }

    public Person findPersonById(int id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

}
