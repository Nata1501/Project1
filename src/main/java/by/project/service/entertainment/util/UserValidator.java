package by.project.service.entertainment.util;


import by.project.service.entertainment.dto.UserDTO;
import by.project.service.entertainment.models.domain.User;
import by.project.service.entertainment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        if(userService.findByLogin(userDTO.getLogin()) != null)
            errors.rejectValue("login", "This login is already taken", "This login is already taken");

        if(userService.findByEmail(userDTO.getEmail()) != null)
            errors.rejectValue("email", "This email is already taken", "This email is already taken");
    }

}
