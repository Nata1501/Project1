package by.project.service.EntertainmentService.services;

import by.project.service.EntertainmentService.util.ObjectNotFoundException;
import by.project.service.EntertainmentService.models.main.Event;
import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.repositories.EntertainmentRepository;
import by.project.service.EntertainmentService.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EntertainmentService entertainmentService;
    private final PlaceService placeService;




    public List<Event> findAll() {
        return eventRepository.findAll();
    }


    public List<Event> findEvents(Type type, boolean relevance) {
        return eventRepository.findByTypeAndRelevance(type, relevance);
    }


    public List<Event> findEventsWithPagination(int page, int eventPerPage, Type type, boolean relevance) { // проверить работу
        return eventRepository.findAll(PageRequest.of(page, eventPerPage)).getContent()
            .stream().filter(event -> event.getType() == type && event.getRelevance() == relevance).collect(Collectors.toList());
    }


    public Event findOne(int id) {
        Optional<Event> event = eventRepository.findById(id);
            return event.orElse(null);
    }


    public List<Event> findByRelevance(boolean relevance) {
        return eventRepository.findByRelevance(relevance);
    }


    @Transactional
    public void saveEvent(Event event) {
        enrichEvent(event);
        eventRepository.save(event);
    }


    @Transactional
    public void checkRelevance(Event event, int timeInMinutes ) {
        Date currentDate = new Date();
        if (event.getDateTime().getTime() - currentDate.getTime() < timeInMinutes)
            event.setRelevance(false);

       eventRepository.save(event);
    }


    private void enrichEvent(Event event) {
        event.setEntertainment((entertainmentService.findOne(event.getEntertainment().getId())));
        event.setPlace(placeService.findPlaceByName(event.getPlace().getName()));
        event.setRelevance(true);
    }


    @Transactional
    public void delete(int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Object not found"));

        eventRepository.deleteById(event.getId());
    }

}
