package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import polytech.group3.iwa.alert_contact_case.models.CovidInfo;
import polytech.group3.iwa.alert_contact_case.models.CovidInfoId;
import polytech.group3.iwa.alert_contact_case.repositories.CaseTypeRepository;
import polytech.group3.iwa.alert_contact_case.repositories.CovidInfoRepository;
import polytech.group3.iwa.alert_contact_case.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private CaseTypeRepository caseTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CovidInfoRepository covidInfoRepository;

    @PostMapping
    @RequestMapping("/declarePositive")
    public CovidInfo newContamination() {

        //Retrieve user Id
        String userid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CovidInfo covidInfo = new CovidInfo(new CovidInfoId(userid, 1, LocalDateTime.now()));
        //Check that all the required fields are not null
        if (!isValidCovidInfo(covidInfo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //Check if the specified case type exists in the database
        if (!caseTypeRepository.existsById(covidInfo.getCovidInfoId().getId_case_type())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //Check if the specified user exists in the database
        if (!userRepository.existsById(covidInfo.getCovidInfoId().getId_keycloak())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //Save a new covid case in the database
        return covidInfoRepository.saveAndFlush(covidInfo);
    }

    public boolean isValidCovidInfo(CovidInfo covidInfo) {
        return covidInfo.getCovidInfoId().getId_case_type() != 0 && !Objects.equals(covidInfo.getCovidInfoId().getId_keycloak(), "") && covidInfo.getCovidInfoId().getReporting_date() != null;
    }

}
