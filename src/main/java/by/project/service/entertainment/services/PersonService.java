package by.project.service.entertainment.services;


import by.project.service.entertainment.models.domain.Director;
import by.project.service.entertainment.models.domain.Person;
import by.project.service.entertainment.repositories.DirectorRepository;
import by.project.service.entertainment.repositories.PersonRepository;
import by.project.service.entertainment.util.ObjectNotFoundException;
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
        return directorRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Director not found by name: " + name));
    }

    public Person findPersonByName(String name) {
        return personRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Person not found by name: " + name));

    }

    public Person findOne(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Person not found by ID: " + id));
    }

}
