package by.project.service.EntertainmentService.controllers;

import by.project.service.EntertainmentService.util.EntertainmentException;
import by.project.service.EntertainmentService.util.ErrorResponse;
import by.project.service.EntertainmentService.util.EventValidator;
import by.project.service.EntertainmentService.dto.EventDTO;
import by.project.service.EntertainmentService.models.main.Event;
import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.services.EventService;
import by.project.service.EntertainmentService.util.ObjectNotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.project.service.EntertainmentService.util.ErrorsUtil.returnErrorsToClient;


@RestController
@RequestMapping("/events")

public class EventsController {

    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;


    @Autowired
    public EventsController(EventService eventService, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }


    @PostConstruct
    public void checkEventRelevance() {
        eventService.findAll().stream().filter(Event::getRelevance).
                forEach(event -> eventService.checkRelevance(event, 1));
    }


    @GetMapping("/movies")
    public ResponseEntity<?> getMovieEvents(@RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "events_per_page", required = false)
                                             Integer eventsPerPage) {
        return getEvents(page, eventsPerPage, Type.MOVIE, true);
    }


    @GetMapping("/archivemovies")
    public ResponseEntity<?> getArchiveMovies(@RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "events_per_page", required = false)
                                              Integer eventsPerPage) {
        return getEvents(page, eventsPerPage, Type.MOVIE, false);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") int id) {
        Event event = eventService.findOne(id);
        return event != null
                ? ResponseEntity.ok(convertToEventDTO(event))
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/add")
    public ResponseEntity<HttpStatus> createEvent(@RequestBody @Valid EventDTO eventDTO, BindingResult bindingResult) {
        Event event = convertToEvent(eventDTO);
        eventValidator.validate(event, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        eventService.saveEvent(event);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        try {
            eventService.delete(id);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    private ResponseEntity<?> getEvents(Integer page, Integer eventsPerPage, Type type, boolean relevance) {
        List<Event> events = new ArrayList<>();

        if ((page == null) || (eventsPerPage == null))
            events = eventService.findEvents(type, relevance);
        else
            events = eventService.findEventsWithPagination(page, eventsPerPage, type, relevance);

        return events != null &&  !events.isEmpty()
                ? ResponseEntity.ok(events.stream().map(this::convertToEventDTO).collect(Collectors.toList()))
                : ResponseEntity.ok().body(HttpStatus.NO_CONTENT);
    }


    private EventDTO convertToEventDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }


    private Event convertToEvent(EventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(EntertainmentException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}