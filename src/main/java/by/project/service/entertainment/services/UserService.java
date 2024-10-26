package by.project.service.entertainment.services;


import by.project.service.entertainment.exception.InsufficientFundsException;
import by.project.service.entertainment.util.ObjectNotFoundException;
import by.project.service.entertainment.models.domain.Event;
import by.project.service.entertainment.models.domain.MainAccount;
import by.project.service.entertainment.models.domain.Order;
import by.project.service.entertainment.models.domain.User;
import by.project.service.entertainment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventService eventService;
    private final MailSenderService mailSenderService;
    private final JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public List<User> findAllWithPagination(int page, int userPerPage) {
        return userRepository.findAll(PageRequest.of(page, userPerPage)).getContent();
    }


    public User findOne(Long id) {
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

    public void saveUser(User user) {
        enrichUser(user);
        userRepository.save(user);
    }


    private void enrichUser(User user) {
        user.setPassword("password");
        user.setCardBalance(BigDecimal.valueOf(100.0));
    }


    @Transactional
    public void delete(Long id) {
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
            String orderId = jdbcTemplate.queryForObject("SELECT id FROM User_Event ORDER BY id DESC LIMIT 1", String.class);
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


    private void withdrawMoney(User user, Event event) {
        BigDecimal totalCost = event.getEntertainment().getPrice().add(MainAccount.serviceFee);

        if (user.getCardBalance().compareTo(totalCost) < 0)
            throw new InsufficientFundsException(user.getCardBalance(), totalCost);

        user.setCardBalance(user.getCardBalance().subtract(totalCost));
    }

}
