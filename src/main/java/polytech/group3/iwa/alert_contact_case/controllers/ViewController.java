package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/home"})
    public String home() { return "index"; }

}
