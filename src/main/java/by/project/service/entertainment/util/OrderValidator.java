package by.project.service.entertainment.util;


import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.Order;
import by.project.service.entertainment.services.EventService;
import by.project.service.entertainment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrderValidator implements Validator {

    private final EventService eventService;
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public OrderValidator(EventService eventService, UserService userService, JdbcTemplate jdbcTemplate) {
        this.eventService = eventService;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Order order = (Order) target;

        if(userService.findOne(order.getUserId()) == null)
            errors.rejectValue("UserId", "", "This userID does not exist");

        Event event = eventService.findOne(order.getEventId());

        if(event == null)
            errors.rejectValue("EventId", "", "This eventID does not exist");
        else if(!event.getRelevance())
            errors.rejectValue("EventId", "", "This event is irrelevant");

        try {
            jdbcTemplate.queryForObject("SELECT id FROM User_Event WHERE id_event=? and seat=? and refund_date_time IS null", Integer.class, order.getEventId(), order.getSeat()).toString();
            errors.rejectValue("seat", "", "Seat number is taken");
        } catch (EmptyResultDataAccessException e) {
            // непонятно, что вообще тут писать
        }
    }

}
