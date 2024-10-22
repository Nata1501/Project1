package by.project.service.EntertainmentService.services;


import by.project.service.EntertainmentService.util.ObjectNotFoundException;
import by.project.service.EntertainmentService.models.main.Event;
import by.project.service.EntertainmentService.models.main.MainAccount;
import by.project.service.EntertainmentService.models.other.Order;
import by.project.service.EntertainmentService.models.person.User;
import by.project.service.EntertainmentService.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventService eventService;
    private final MailSenderService mailSenderService;
    private JdbcTemplate jdbcTemplate;



    public List<User> findAll() {
        return userRepository.findAll();
    }


    public List<User> findAllWithPagination(int page, int userPerPage) {
        return userRepository.findAll(PageRequest.of(page, userPerPage)).getContent();
    }


    public User findOne(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }


    public User findByLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user.orElse(null);
    }


    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }


    @Transactional
    public void saveUser(User user) {
        enrichUser(user);
        userRepository.save(user);
    }


    private void enrichUser(User user) {
        user.setPassword("password");
        user.setCardBalance(100.0);
    }


    @Transactional
    public void delete(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Object not found"));

        userRepository.deleteById(user.getId());
    }


    @Transactional
    public void saveUserEvent(Order order) {
        User user = findOne(order.getUserId());
        Event event = eventService.findOne(order.getEventId());

        try {
            withdrawMoney(user, event);
            jdbcTemplate.update("INSERT INTO User_Event(id_user, id_event, seat, service_fee, payment_date_time) " +
                                "VALUES(?, ?, ?, ?, ?)",
                                order.getUserId(), order.getEventId(), order.getSeat(), MainAccount.serviceFee,
                                java.time.LocalDateTime.now());
            String orderId = jdbcTemplate.queryForObject("SELECT id FROM User_Event ORDER BY id DESC LIMIT 1",
                                                         String.class).toString();
            sendMessage(user, event, orderId, order.getSeat());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private void sendMessage(User user, Event event, String orderId, int seat) {
        String message = "Уважаемый пользователь! Благодарим Вас за заказ." +
                "\nДетали заказа: " + "\nВаш номер заказа: " + orderId +
                "\nНазвание: " + event.getEntertainment().getName() +
                "\nМесто проведения: " + event.getPlace().getName() +
                " Номер места: " + seat +
                "\nЦена: " + event.getEntertainment().getPrice() + "руб." +
                " Комиссионный сбор: " + MainAccount.serviceFee + "руб." +
                "\nТелефоны для справок: +375 29 8888888";

        mailSenderService.sendMessage(user.getEmail(),"Order details", message,"nata1818183@gmail.com");
    }


    private void withdrawMoney(User user, Event event) throws Exception {
        Double totalCost = event.getEntertainment().getPrice() + MainAccount.serviceFee;

        if (user.getCardBalance() < totalCost)    // try - catch ????????????????????
            throw new Exception("Insufficient funds");

        user.setCardBalance(user.getCardBalance() - totalCost);
    }

}
