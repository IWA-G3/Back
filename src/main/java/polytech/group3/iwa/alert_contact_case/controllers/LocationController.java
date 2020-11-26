package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import polytech.group3.iwa.alert_contact_case.config.KafkaSender;
import polytech.group3.iwa.alert_contact_case.models.Location;
import polytech.group3.iwa.alert_contact_case.models.LocationKafka;
import polytech.group3.iwa.alert_contact_case.repositories.LocationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("/dangerous")
    public List<Location> listDangerous(@RequestParam(value = "longitude") double longitude, @RequestParam(value = "latitude") double latitude, @RequestParam(value = "timestamp") String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return locationRepository.findDangerousLocation(longitude, latitude, LocalDateTime.parse(timestamp, formatter));
    }

    @PostMapping("/add")
    public void addLocation(@RequestParam(value = "longitude") double longitude, @RequestParam(value = "latitude") double latitude) {

        String userid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocationKafka message = new LocationKafka(latitude, longitude, LocalDateTime.now().toString().substring(0, 19), userid);
        kafkaSender.sendMessage(message, "location");
    }

    @PostMapping("/addDangerous")
    public void addDangerousLocation( @RequestParam(value = "longitude") double longitude, @RequestParam(value = "latitude") double latitude, @RequestParam(value = "timestamp") String timestamp) {
        String userid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocationKafka message = new LocationKafka(latitude, longitude, timestamp, userid);
        System.out.println("envoi de localisation dangereuse");
        kafkaSender.sendMessage(message, "dangerous_location");
    }


}
