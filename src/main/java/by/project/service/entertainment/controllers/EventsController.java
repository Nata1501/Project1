package by.project.service.entertainment.controllers;

import by.project.service.entertainment.dto.EventDTO;
import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.Type;
import by.project.service.entertainment.services.EventService;
import by.project.service.entertainment.util.EntertainmentException;
import by.project.service.entertainment.util.ErrorResponse;
import by.project.service.entertainment.util.EventValidator;
import by.project.service.entertainment.util.ObjectNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static by.project.service.entertainment.util.ErrorsUtil.returnErrorsToClient;


// TODO убрать всю логику из контроллеров!
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {

    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @GetMapping("/movies")
    public ResponseEntity<List<EventDTO>> getMovieEvents(@RequestParam(value = "page", required = false) Integer page,
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
    public ResponseEntity<EventDTO> getEvent(@PathVariable("id") Long id) {
        Event event = eventService.findOne(id);
        return event != null
                ? ResponseEntity.ok(convertToEventDTO(event))
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/add")
    public ResponseEntity<Void> createEvent(@RequestBody @Valid EventDTO eventDTO, BindingResult bindingResult) {
        Event event = convertToEvent(eventDTO);
        eventValidator.validate(event, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        eventService.saveEvent(event);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            eventService.delete(id);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    private ResponseEntity<List<EventDTO>> getEvents(Integer page, Integer eventsPerPage, Type type, boolean relevance) {
        List<Event> events;

        if ((page == null) || (eventsPerPage == null))
            events = eventService.findEvents(type, relevance);
        else
            events = eventService.findEventsWithPagination(page, eventsPerPage, type, relevance);

        return events != null &&  !events.isEmpty()
                ? ResponseEntity.ok(events.stream().map(this::convertToEventDTO).collect(Collectors.toList()))
                : ResponseEntity.noContent().build();
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