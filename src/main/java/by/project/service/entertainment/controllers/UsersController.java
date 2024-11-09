package by.project.service.entertainment.controllers;


import by.project.service.entertainment.dto.EntertainmentDTO;
import by.project.service.entertainment.util.*;
import by.project.service.entertainment.dto.UserDTO;
import by.project.service.entertainment.models.domain.Order;
import by.project.service.entertainment.models.domain.User;
import by.project.service.entertainment.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static by.project.service.entertainment.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final OrderValidator orderValidator;

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "users_per_page", required = false) Integer usersPerPage) {
        List<User> users = userService.findAll(page, usersPerPage);
        return ResponseEntity.ok(users.stream().map(this::convertToUserDTO).collect(Collectors.toList()));
    }


    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(convertToUserDTO(userService.findOne(id)));
    }


    @PostMapping("/add")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        userService.saveUser(convertToUser(userDTO));
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/makeOrder")
    public ResponseEntity<Void> makeOrder(@RequestBody Order order, BindingResult bindingResult) {
        orderValidator.validate(order, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        userService.saveUserEvent(order);
        return ResponseEntity.ok().build();
    }


    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }


    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
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
