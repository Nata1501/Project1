package by.project.service.entertainment.services;

import by.project.service.entertainment.util.ObjectNotFoundException;
import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.Type;
import by.project.service.entertainment.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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


    public Event findOne(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found by ID: " + id));
    }


    public List<Event> findByRelevance(boolean relevance) {
        return eventRepository.findByRelevance(relevance);
    }


    public void saveEvent(Event event) {
        enrichEvent(event);
        eventRepository.save(event);
    }

    public void checkAndUpdateRelevance(Long totalTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Event> updatedEvents = eventRepository.findAll().stream()
                .map(event -> {
                    if (event.getDateTime().plusMinutes(totalTime).isAfter(currentDateTime)) {
                        event.setRelevance(false);
                    }
                    return event;
                }).toList();
       eventRepository.saveAll(updatedEvents);
    }


    private void enrichEvent(Event event) {
        event.setEntertainment(entertainmentService.findOne(event.getEntertainment().getId()));
        event.setPlace(placeService.findPlaceByName(event.getPlace().getName()));
        event.setRelevance(true);
    }


    @Transactional
    public void delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Object not found"));

        eventRepository.deleteById(event.getId());
    }

}
