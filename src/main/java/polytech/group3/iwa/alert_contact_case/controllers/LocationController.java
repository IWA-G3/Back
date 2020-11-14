package polytech.group3.iwa.alert_contact_case.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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

    @GetMapping
    public List<Location> list() {
        System.out.println(LocalDateTime.now());
        return locationRepository.findAll();
    }

    @GetMapping
    @RequestMapping("/dangerous")
    public List<Location> listDangerous(@RequestParam(value="longitude") double longitude, @RequestParam(value="latitude") double latitude, @RequestParam(value="timestamp") String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println(timestamp);
        return locationRepository.findDangerousLocation(longitude, latitude, LocalDateTime.parse(timestamp, formatter));
    }

    @PostMapping
    @RequestMapping("/add")
    public void addLocation(@RequestParam(value="userid") int userid, @RequestParam(value="longitude") double longitude, @RequestParam(value="latitude") double latitude, @RequestParam(value="timestamp") String timestamp){
        LocationKafka message = new LocationKafka(longitude, latitude, timestamp, userid);
        kafkaSender.sendMessage(message, "location");
    }

}
