package polytech.group3.iwa.alert_contact_case.controllers;

//import org.springframework.beans.factory.annotation.Autowired;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import polytech.group3.iwa.alert_contact_case.models.Login;
//import polytech.group3.iwa.alert_contact_case.models.Registration;
//
//import javax.validation.Valid;
//import java.io.IOException;
//import java.util.Map;

@Controller
public class ViewController {

    @GetMapping({""})
    public String getIndex() {

        return "index";
    }

    @GetMapping({"home"})
    public String getHome() {
        return "home";
    }
//
//    @GetMapping({"/login"})
//    public String registration(Login login) {
//        return "login";
//    }
//
//    @GetMapping({"/register"})
//    public String register(Registration registration) {
//        return "registration";
//    }
//
//    @PostMapping({"/doLogin"})
//    public String login(@Valid @ModelAttribute("login") Login login, BindingResult result, Map<String, Object> model) {
//
////        System.out.println("Login");
//
//        if (result.hasErrors()) {
//            model.put("error", true);
//            return "login";
//        }
//
////        Optional<String> access_token = Optional.empty();
////
////        try {
////            access_token = kck.getJWT(login.getEmail(), login.getPassword());
////        } catch (KeycloakService.InvalidParameter | IOException | InterruptedException invalidParameter) {
////            invalidParameter.printStackTrace();
////            model.put("error", true);
////            return "login";
////        }
////
////        if (access_token.isEmpty()) {
////            model.put("error", true);
////            return "login";
////        }
////
////        String token = access_token.get();
////
////        model.put("token", token);
//
//        return "redirect:home";
//    }
//
//    @PostMapping("/registration")
//    public String registration(@Valid @ModelAttribute("registration") Registration registration, BindingResult result, Map<String, Object> model) {
//        System.out.println(registration);
//        System.out.println(result);
//
//        if (result.hasErrors()) {
//            model.put("error", true);
//            return "registration";
//        }
//
//        if (!registration.getPassword().equals(registration.getPassword_confirm())) {
//            model.put("password_error", "Vos mots de passes doivent co\u00efncider");
//            return "registration";
//        }
//
//        boolean success = false;
//        try {
//            success = kck.addUser(registration.getEmail(), registration.getPassword());
//        } catch (KeycloakService.InvalidParameter | IOException | InterruptedException | KeycloakService.InvalidAdminAccess | KeycloakService.NoLocation | KeycloakService.NoPasswordSet invalidParameter) {
//            invalidParameter.printStackTrace();
//            model.put("error", true);
//            return "login";
//        }
//
//        if (!success) {
//            model.put("error", true);
//            return "login";
//        }
//
////        String token = access_token.get();
////
//
//        model.put("register", true);
//
//        return "redirect:login";
//    }

}
