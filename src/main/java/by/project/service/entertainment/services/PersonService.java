package by.project.service.entertainment.services;


import by.project.service.entertainment.models.domain.Director;
import by.project.service.entertainment.models.domain.Person;
import by.project.service.entertainment.repositories.DirectorRepository;
import by.project.service.entertainment.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
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
    }

    public Person findPersonByName(String name) {
        Optional<Person> person = personRepository.findByName(name);
        return person.orElse(null);
    }

    public Person findPersonById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

}
