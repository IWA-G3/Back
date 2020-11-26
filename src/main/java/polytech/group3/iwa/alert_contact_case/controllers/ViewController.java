package polytech.group3.iwa.alert_contact_case.controllers;

import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import polytech.group3.iwa.alert_contact_case.models.Login;
import polytech.group3.iwa.alert_contact_case.models.Registration;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class ViewController {


    @Autowired
    public KeycloakService kck;

    @GetMapping({"", "home"})
    @RolesAllowed("user")
    public String getHome() {
        return "home";
    }

    @GetMapping({"/login"})
    public String registration(Login login) {
        return "login";
    }

    @GetMapping({"/register"})
    public String register(Registration registration) {
        return "registration";
    }

    @PostMapping({"/login"})
    public String login(@Valid @ModelAttribute("login") Login login, BindingResult result, Map<String, Object> model) {
        if (result.hasErrors()) {
            return "login";
        }

        Optional<String> access_token = Optional.empty();

        try {
            access_token = kck.getJWT(login.getEmail(), login.getPassword());
        } catch (KeycloakService.InvalidParameter | IOException | InterruptedException invalidParameter) {
            invalidParameter.printStackTrace();
            return "login?error";
        }

        if (access_token.isEmpty()) {
            return "login?error";
        }

        String token = access_token.get();
        model.put("token", token);

        // Todo : set access token en header dans le navigateur ?

        return "redirect:home";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("registration") Registration registration, BindingResult result, Map<String, Object> model) {
        System.out.println(registration);
        System.out.println(result);
        if (result.hasErrors()) {
            System.out.println("Error");
            return "registration";
        }
        if (!registration.getPassword().equals(registration.getPassword_confirm())) {
            model.put("password_error", "Vos mots de passes doivent co\u00efncider");
            return "registration";
        }

        Optional<String> access_token = Optional.empty();
        try {
            access_token = kck.addUser(registration.getEmail(), registration.getPassword());
        } catch (KeycloakService.InvalidParameter | IOException | InterruptedException | KeycloakService.InvalidAdminAccess | KeycloakService.NoLocation | KeycloakService.NoPasswordSet invalidParameter) {
            invalidParameter.printStackTrace();
            return "login?error";
        }

        if (access_token.isEmpty()) {
            return "login?error";
        }

        String token = access_token.get();

        model.put("token", token);
        // Todo : set access token en header dans le navigateur ?

        return "redirect:home";
    }

}
