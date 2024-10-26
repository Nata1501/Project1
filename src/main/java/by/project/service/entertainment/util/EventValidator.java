package by.project.service.entertainment.util;

import by.project.service.entertainment.dto.EventDTO;
import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.Place;
import by.project.service.entertainment.services.EntertainmentService;
import by.project.service.entertainment.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class EventValidator implements Validator {

    private final EntertainmentService entertainmentService;
    private final PlaceService placeService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EventValidator(EntertainmentService entertainmentService, PlaceService placeService, JdbcTemplate jdbcTemplate) {
        this.entertainmentService = entertainmentService;
        this.placeService = placeService;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return EventDTO.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Event event = (Event) target;

        // переделать
        if (entertainmentService.findOne(event.getEntertainment().getId()) == null)
            errors.rejectValue("entertainment", "", "This entertainment does not exist");

        // для чего эта проверка?
        //        Date currentDate = new Date();        //     упростить (есть спец метод)
        //        if (event.getDateTime().getTime() - currentDate.getTime() <= 1440)
        //            errors.rejectValue("dateTime", "", "The time is not correct");


        // Создаю переменную place, потому что позже она понадобится
        Place place = placeService.findPlaceByName(event.getPlace().getName());
        if (place == null)
            errors.rejectValue("place", "", "This place does not exist");
        else {
            try {
                jdbcTemplate.queryForObject("SELECT id FROM Event WHERE id_place=? and date_time=?", Integer.class, place.getId(), event.getDateTime()).toString();
                errors.rejectValue("dateTime", "", "The place and date are taken");
            } catch (EmptyResultDataAccessException e) {
                throw new RuntimeException(e);
               // непонятно, что вообще тут писать
            }
        }
    }
}