package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import polytech.group3.iwa.alert_contact_case.models.User;
import polytech.group3.iwa.alert_contact_case.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    public UserRepository userRepository;

    @GetMapping
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @RequestMapping("/notify")
    public void sendEmail(@RequestParam(value="email") String email) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("IMPORTANT - COVID ALERT");
        message.setText("Hello, you have recently added a location to our application. This location was recently frequented by a person positive to covid-19. You are now considered as a contact case. Please, be tested as soon as possible.");
        this.emailSender.send(message);
    }

    @GetMapping
    @RequestMapping("/contacts")
    public void getContacts(@RequestParam(value="longitude") double longitude, @RequestParam(value="latitude") double latitude, @RequestParam(value="timestamp") String timestamp) {


    }

}
