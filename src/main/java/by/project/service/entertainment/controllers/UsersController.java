package by.project.service.entertainment.controllers;


import by.project.service.entertainment.util.*;
import by.project.service.entertainment.dto.UserDTO;
import by.project.service.entertainment.models.domain.Order;
import by.project.service.entertainment.models.domain.User;
import by.project.service.entertainment.services.UserService;
import jakarta.validation.Valid;
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
public class UsersController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final OrderValidator orderValidator;


    @Autowired
    public UsersController(UserService userService, ModelMapper modelMapper, UserValidator userValidator, OrderValidator orderValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.orderValidator = orderValidator;
    }



    @GetMapping()
    public ResponseEntity<?> getUsers(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "users_per_page", required = false) Integer usersPerPage) {
        List<User> users;

        if ((page == null) || (usersPerPage == null))
            users = userService.findAll();
        else
            users = userService.findAllWithPagination(page, usersPerPage);

        return users != null &&  !users.isEmpty()
                ? ResponseEntity.ok(users.stream().map(this::convertToUserDTO)
                .collect(Collectors.toList()))
                : ResponseEntity.ok().body(HttpStatus.NO_CONTENT);
    }


    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        return user != null
                ? ResponseEntity.ok(convertToUserDTO(user))
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/add")
    public ResponseEntity<HttpStatus>  createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        userService.saveUser(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
       try {
            userService.delete(id);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/makeOrder")
    public ResponseEntity<HttpStatus> createUser(@RequestBody Order order, BindingResult bindingResult) {
        orderValidator.validate(order, bindingResult);

        if(bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        userService.saveUserEvent(order);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }


    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
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
