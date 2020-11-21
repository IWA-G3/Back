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
    public UserRepository userRepository;

}
