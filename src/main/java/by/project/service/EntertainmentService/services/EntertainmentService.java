package by.project.service.EntertainmentService.services;


import by.project.service.EntertainmentService.repositories.DirectorRepository;
import by.project.service.EntertainmentService.repositories.PersonRepository;
import by.project.service.EntertainmentService.util.EntertainmentException;
import by.project.service.EntertainmentService.util.ObjectNotFoundException;
import by.project.service.EntertainmentService.models.main.Entertainment;
import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.models.person.Director;
import by.project.service.EntertainmentService.models.person.Person;
import by.project.service.EntertainmentService.repositories.EntertainmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EntertainmentService {

    private final EntertainmentRepository entertainmentRepository;
    private final PersonRepository personRepository;
    private final DirectorRepository directorRepository;
    private final PersonService personService;




    public List<Entertainment> findAll(Type type) {
        return entertainmentRepository.findByType(type);
    }


    public List<Entertainment> findAllWithPagination(int page, int entertainmentsPerPage, Type type) {
        return entertainmentRepository.findAll(PageRequest.of(page, entertainmentsPerPage)).getContent()
                .stream().filter(entertainment -> entertainment.getType() == type).collect(Collectors.toList());
    }


    public Entertainment findOne(int id) {
        Optional<Entertainment> entertainment = entertainmentRepository.findById(id);
        return entertainment.orElse(null);
    }


    public Entertainment findEntertainment(int id, Type type) {
        Optional<Entertainment> entertainment = entertainmentRepository.findByTypeAndId(type, id);
        return entertainment.orElse(null);
    }


    public Entertainment findEntertainment(String name, Type type, int year) {
        Optional<Entertainment> entertainment = entertainmentRepository.findByNameAndTypeAndYear(name, type, year);
        return entertainment.orElse(null);
    }


    @Transactional
    public void saveEntertainment(Entertainment entertainment, Type type) {
        entertainment.setType(type);
        enrichEntertainment(entertainment);
        entertainmentRepository.save(entertainment);
    }


    @Transactional
    public void delete(int id) {
        Entertainment entertainment = entertainmentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found"));

        entertainmentRepository.deleteById(entertainment.getId());
    }


    @Transactional
    public void update(int id, Entertainment updatedEntertainment) {
        updatedEntertainment.setId(id);
        Director director = directorRepository.findByName(updatedEntertainment.getDirector().getName()).
                orElseThrow(() -> new ObjectNotFoundException("Director not found"));
        updatedEntertainment.setDirector(director);

        entertainmentRepository.save(updatedEntertainment);
    }


    private boolean containsPerson(int entertainmentId, int personId) {
        Entertainment entertainment = entertainmentRepository.findById(entertainmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found"));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException("Person not found"));

        return entertainment.getPerformers().contains(person);
    }


    private void enrichEntertainment(Entertainment entertainment) {   //??????
        entertainment.setDirector(personService.findDirectorByName(entertainment.getDirector().getName()));

        List<Person> personList = new ArrayList<>();

        for(int i=0; i<entertainment.getPerformers().size(); i++) {  // переписать через lambda???
            Person person = personService.findPersonByName(entertainment.getPerformers().get(i).getName());
            personList.add(person);
            person.getEntertainments().add(entertainment);
        }
        entertainment.setPerformers(personList);
    }


    @Transactional
    public void addPerson(int id, Integer personId) {
        Entertainment entertainment = entertainmentRepository.findById(id)
                .orElseThrow(() -> new EntertainmentException("Entertainment not found"));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntertainmentException("Person not found"));

        if (!entertainment.getPerformers().contains(person)) {
            entertainment.getPerformers().add(person);
            person.getEntertainments().add(entertainment);
        } else
            throw new EntertainmentException("Entertainment already exists the person");
    }


    @Transactional
    public void removePerson(int id, Integer personId) {
        Entertainment entertainment = entertainmentRepository.findById(id)
                .orElseThrow(() -> new EntertainmentException("Entertainment not found"));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntertainmentException("Person not found"));

        if (entertainment.getPerformers().contains(person)) {
            entertainment.getPerformers().remove(person);
            person.getEntertainments().remove(entertainment);
        } else
            throw new EntertainmentException("Entertainment does not exist the person");
    }

}
