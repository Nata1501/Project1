package by.project.service.entertainment.controllers;

import by.project.service.entertainment.dto.EventDTO;
import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.Type;
import by.project.service.entertainment.services.EventService;
import by.project.service.entertainment.util.ErrorResponse;
import by.project.service.entertainment.util.EventValidator;
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
        List<Event> events = eventService.findEvents(page, eventsPerPage, Type.MOVIE, true);
        return ResponseEntity.ok(events.stream().map(this::convertToEventDTO).collect(Collectors.toList()));
    }


    @GetMapping("/archive-movies")
    public ResponseEntity<List<EventDTO>> getArchiveMovies(@RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "events_per_page", required = false)
                                              Integer eventsPerPage) {
        List<Event> events = eventService.findEvents(page, eventsPerPage, Type.MOVIE, false);
        return ResponseEntity.ok(events.stream().map(this::convertToEventDTO).collect(Collectors.toList()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(convertToEventDTO(eventService.findOne(id)));
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
        eventService.delete(id);
        return ResponseEntity.ok().build();
    }



    private EventDTO convertToEventDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }


    private Event convertToEvent(EventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}