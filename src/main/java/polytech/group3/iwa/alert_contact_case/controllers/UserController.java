package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import polytech.group3.iwa.alert_contact_case.models.CaseType;
import polytech.group3.iwa.alert_contact_case.models.CovidInfo;
import polytech.group3.iwa.alert_contact_case.models.CovidInfoId;
import polytech.group3.iwa.alert_contact_case.models.Location;
import polytech.group3.iwa.alert_contact_case.repositories.CaseTypeRepository;
import polytech.group3.iwa.alert_contact_case.repositories.CovidInfoRepository;
import polytech.group3.iwa.alert_contact_case.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private CaseTypeRepository caseTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CovidInfoRepository covidInfoRepository;

    @PostMapping
    @RequestMapping("/notify")
    public void sendEmail(@RequestParam(value="email") String email) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("IMPORTANT - COVID ALERT");
        message.setText("Hello, you have recently added a location to our application. This location was recently frequented by a person positive to covid-19. You are now considered as a contact case. Please, be tested as soon as possible.");
        this.emailSender.send(message);

    }

    @PostMapping
    @RequestMapping("/declarePositive")
    public CovidInfo newContamination(@RequestBody CovidInfo covidInfo) {
        // TODO : Implement Keycloak annotation

        //Check that all the required fields are not null
        if (!isValidCovidInfo(covidInfo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //Check if the specified case type exists in the database
        if(!caseTypeRepository.existsById(covidInfo.getCovidInfoId().getId_case_type())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //Check if the specified user exists in the database
        if (!userRepository.existsById(covidInfo.getCovidInfoId().getId_keycloak())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //Save a new covid case in the database
        return covidInfoRepository.saveAndFlush(covidInfo);
    }

    @GetMapping
    @RequestMapping("/test")
    public List<CovidInfo> listDangerous() {
        return covidInfoRepository.findAll();
    }

    public boolean isValidCovidInfo(CovidInfo covidInfo) {
        return covidInfo.getCovidInfoId().getId_case_type() != 0 && covidInfo.getCovidInfoId().getId_case_type() != 0 && covidInfo.getCovidInfoId().getReporting_date() != null;
    }

}
