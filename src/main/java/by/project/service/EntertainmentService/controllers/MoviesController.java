package by.project.service.EntertainmentService.controllers;


import by.project.service.EntertainmentService.util.EntertainmentException;
import by.project.service.EntertainmentService.util.EntertainmentValidator;
import by.project.service.EntertainmentService.util.ErrorResponse;
import by.project.service.EntertainmentService.dto.EntertainmentDTO;
import by.project.service.EntertainmentService.models.main.Entertainment;
import by.project.service.EntertainmentService.models.other.Type;
import by.project.service.EntertainmentService.services.EntertainmentService;
import by.project.service.EntertainmentService.util.ObjectNotFoundException;
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

import static by.project.service.EntertainmentService.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    private final EntertainmentService entertainmentService;
    private final ModelMapper modelMapper;
    private final EntertainmentValidator entertainmentValidator;

    @Autowired
    public MoviesController(EntertainmentService entertainmentService, ModelMapper modelMapper, EntertainmentValidator entertainmentValidator) {
        this.entertainmentService = entertainmentService;
        this.modelMapper = modelMapper;
        this.entertainmentValidator = entertainmentValidator;
    }




    @GetMapping()
    public ResponseEntity<?> getAllMovies(@RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "entertainments_per_page", required = false) Integer entertainmentsPerPage)
    {
        List<Entertainment> movies = new ArrayList<>();

        if ((page == null) || (entertainmentsPerPage == null))
            movies = entertainmentService.findAll(Type.MOVIE);
        else
             movies = entertainmentService.findAllWithPagination(page, entertainmentsPerPage, Type.MOVIE);

        return movies != null &&  !movies.isEmpty()
                ? ResponseEntity.ok(movies.stream().map(this::convertToEntertainmentDTO)
                                    .collect(Collectors.toList()))
                : ResponseEntity.ok().body(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMovie(@PathVariable("id") int id) {
        Entertainment entertainment = entertainmentService.findEntertainment(id, Type.MOVIE);
        return entertainment != null
                ? ResponseEntity.ok(convertToEntertainmentDTO(entertainment))
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid EntertainmentDTO entertainmentDTO,
                            BindingResult bindingResult) {
        Entertainment entertainment = convertToEntertainment(entertainmentDTO);
        entertainmentValidator.validate(entertainment, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        entertainmentService.saveEntertainment(entertainment, Type.MOVIE);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        try {
            entertainmentService.delete(id);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid EntertainmentDTO entertainmentDTO, BindingResult bindingResult,
                                             @PathVariable("id") int id) {
        entertainmentValidator.validate(bindingResult, entertainmentDTO);

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        entertainmentService.update(id, convertToEntertainment(entertainmentDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PatchMapping("/edit/addPerson/{id}")
    public ResponseEntity<HttpStatus> addPerson(@PathVariable("id") int id,
                                                @RequestParam(value = "personId", required = false) Integer personId) {
        entertainmentService.addPerson(id, personId);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/edit/removePerson/{id}")
    public ResponseEntity<HttpStatus> removePerson(@PathVariable("id") int id,
                                                @RequestParam(value = "personId", required = false) Integer personId) {
        entertainmentService.removePerson(id, personId);
        return ResponseEntity.ok().build();
    }


    private Entertainment convertToEntertainment(EntertainmentDTO entertainmentDTO) {
        return modelMapper.map(entertainmentDTO, Entertainment.class);
    }


    private EntertainmentDTO convertToEntertainmentDTO(Entertainment entertainment) {
        return modelMapper.map(entertainment, EntertainmentDTO.class);
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
