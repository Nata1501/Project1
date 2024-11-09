package by.project.service.entertainment.controllers;


import by.project.service.entertainment.dto.EntertainmentDTO;
import by.project.service.entertainment.models.domain.Entertainment;
import by.project.service.entertainment.models.domain.Type;
import by.project.service.entertainment.services.EntertainmentService;
import by.project.service.entertainment.util.ErrorResponse;
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
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final EntertainmentService entertainmentService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<EntertainmentDTO>> getAllMovies(@RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "entertainments_per_page", required = false) Integer entertainmentsPerPage)
    {
        List<Entertainment> movies = entertainmentService.findEntertainments(page, entertainmentsPerPage, Type.MOVIE);
        return ResponseEntity.ok(movies.stream().map(this::convertToEntertainmentDTO).collect(Collectors.toList()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntertainmentDTO> getMovie(@PathVariable("id") Long id) {
        Entertainment entertainment = entertainmentService.findEntertainment(id, Type.MOVIE);
        return ResponseEntity.ok(convertToEntertainmentDTO(entertainment));
    }


    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody @Valid EntertainmentDTO entertainmentDTO,
                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        entertainmentService.saveEntertainment(convertToEntertainment(entertainmentDTO), Type.MOVIE);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        entertainmentService.delete(id);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/edit/{id}")
    public ResponseEntity<Void> update(@RequestBody @Valid EntertainmentDTO entertainmentDTO, BindingResult bindingResult,
                                             @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        entertainmentService.update(id, convertToEntertainment(entertainmentDTO));
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/edit/addPerson/{id}")
    public ResponseEntity<Void> addPerson(@PathVariable("id") Long id,
                                                @RequestParam(value = "personId", required = false) Long personId) {
        entertainmentService.addPerson(id, personId);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/edit/removePerson/{id}")
    public ResponseEntity<Void> removePerson(@PathVariable("id") Long id,
                                                @RequestParam(value = "personId", required = false) Long personId) {
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
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
