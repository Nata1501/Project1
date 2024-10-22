package by.project.service.EntertainmentService.util;

import by.project.service.EntertainmentService.dto.EntertainmentDTO;
import by.project.service.EntertainmentService.models.main.Entertainment;
import by.project.service.EntertainmentService.services.EntertainmentService;
import by.project.service.EntertainmentService.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EntertainmentValidator implements Validator {

    private final EntertainmentService entertainmentService;
    private final PersonService personService;

    @Autowired
    public EntertainmentValidator(EntertainmentService entertainmentService, PersonService personService) {
        this.entertainmentService = entertainmentService;
        this.personService = personService;
    }




    @Override
    public boolean supports(Class<?> clazz) {
        return EntertainmentDTO.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Entertainment entertainment = (Entertainment) target;

        if(entertainmentService.findEntertainment(entertainment.getName(), entertainment.getType(), entertainment.getYear()) != null)
            errors.rejectValue("name", "", "This enterteinment already exists");

        if(personService.findDirectorByName(entertainment.getDirector().getName()) == null)
            errors.rejectValue("director", "", "This director does not exist");
    }



    public void validate(Errors errors, Object target) {
        EntertainmentDTO entertainmentDTO = (EntertainmentDTO) target;

        if(personService.findDirectorByName(entertainmentDTO.getDirector().getName()) == null)
            errors.rejectValue("director", "", "This director does not exist");
    }

}
