package by.project.service.entertainment.services;


import by.project.service.entertainment.exception.ObjectFoundException;
import by.project.service.entertainment.repositories.DirectorRepository;
import by.project.service.entertainment.repositories.PersonRepository;
import by.project.service.entertainment.util.EntertainmentException;
import by.project.service.entertainment.util.ObjectNotFoundException;
import by.project.service.entertainment.models.domain.Entertainment;
import by.project.service.entertainment.models.domain.Type;
import by.project.service.entertainment.models.domain.Director;
import by.project.service.entertainment.models.domain.Person;
import by.project.service.entertainment.repositories.EntertainmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public List<Entertainment> findEntertainments(Integer page, Integer entertainmentsPerPage, Type type) {
        if ((page == null) || (entertainmentsPerPage == null))
            return findAll(type);
        else
            return findAllWithPagination(page, entertainmentsPerPage, type);
    }


    public List<Entertainment> findAllWithPagination(int page, int entertainmentsPerPage, Type type) {
        return entertainmentRepository.findAll(PageRequest.of(page, entertainmentsPerPage)).getContent()
                .stream().filter(entertainment -> entertainment.getType() == type).collect(Collectors.toList());
    }


    public Entertainment findOne(Long id) {
        return entertainmentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found by ID: " + id));
    }


    public Entertainment findEntertainment(Long id, Type type) {
        return entertainmentRepository.findByTypeAndId(type, id)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found by type: '" + type + "' and ID: " + id));
    }

    public Entertainment findByName(String name) {
        return entertainmentRepository.findByName(name)
            .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found by name: '" + name));
    }


    public Entertainment findEntertainment(String name, Type type, LocalDateTime dateTime) {
        return entertainmentRepository.findByNameAndTypeAndDateTime(name, type, dateTime)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found by name: " + name
                        + ", type: " + type + " and date: " + dateTime));
    }


    private boolean ifExist(String name, Type type, LocalDateTime dateTime) {
        return !entertainmentRepository.findByNameAndTypeAndDateTime(name, type, dateTime).isEmpty();
    }


    public boolean checkEntertainmentExist(String name, Type type, LocalDateTime dateTime) {
        if (ifExist(name, type, dateTime))
            throw new ObjectFoundException("Entertainment by name: " + name
                    + ", type: " + type + " and date: " + dateTime + " exists");
        else
            return false;
    }


   @Transactional
    public void saveEntertainment(Entertainment entertainment, Type type) {
        validateEntertainment(entertainment);
        entertainment.setType(type);
        enrichEntertainment(entertainment);
        entertainmentRepository.save(entertainment);
    }



    @Transactional
    public void delete(Long id) {
        Entertainment entertainment = entertainmentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found"));

        entertainmentRepository.deleteById(entertainment.getId());
    }


    @Transactional
    public void update(Long id, Entertainment updatedEntertainment) {
        updatedEntertainment.setId(id);
        Director director = directorRepository.findByName(updatedEntertainment.getDirector().getName()).
                orElseThrow(() -> new ObjectNotFoundException("Director not found by name: " + updatedEntertainment.getDirector().getName()));
        updatedEntertainment.setDirector(director);

        entertainmentRepository.save(updatedEntertainment);
    }


    private boolean containsPerson(Long entertainmentId, Long personId) {
        Entertainment entertainment = entertainmentRepository.findById(entertainmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Entertainment not found"));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ObjectNotFoundException("Person not found"));

        return entertainment.getPerformers().contains(person);
    }


    private void enrichEntertainment(Entertainment entertainment) {
        entertainment.setDirector(personService.findDirectorByName(entertainment.getDirector().getName()));

        List<Person> personList = new ArrayList<>();

        for(int i = 0; i < entertainment.getPerformers().size(); i++) {
            Person person = personService.findPersonByName(entertainment.getPerformers().get(i).getName());
            personList.add(person);
            person.getEntertainments().add(entertainment);
        }
        entertainment.setPerformers(personList);
    }

    private void validateEntertainment(Entertainment entertainment) {
        checkEntertainmentExist(entertainment.getName(), entertainment.getType(), entertainment.getDateTime());
        personService.findDirectorByName(entertainment.getDirector().getName());
    }


    @Transactional
    public void addPerson(Long id, Long personId) {
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
    public void removePerson(Long id, Long personId) {
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
