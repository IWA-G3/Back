package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import polytech.group3.iwa.alert_contact_case.models.Login;
import polytech.group3.iwa.alert_contact_case.models.Registration;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class ViewController {

    @GetMapping({"", "home"})
    @RolesAllowed("user")
    public String getHome() {
        return "home";
    }

    @GetMapping({"/login"})
    public String login(Login login) { return "login"; }

    @GetMapping({"/register"})
    public String register(Registration registration) { return "registration"; }

    @PostMapping({"login"})
    public String doLogin(@Valid @ModelAttribute("login") Login login, BindingResult result) {
        if(result.hasErrors()) {
            return "login";
        }
        return "redirect:home";
    }

    @PostMapping("registration")
    public String login(@Valid @ModelAttribute("registration") Registration registration, BindingResult result, Map<String, Object> model) {
        System.out.println(registration);
        System.out.println(result);
        if(result.hasErrors()) {
            return "registration";
        }
        if(!registration.getPassword().equals(registration.getPassword_confirm())) {
            model.put("password_error", "Vos mots de passes doivent co\u00efncider");
            return "registration";
        }
        return "redirect:home";
    }

}
