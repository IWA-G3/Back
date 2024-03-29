package polytech.group3.iwa.back.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import polytech.group3.iwa.back.models.Location;
import polytech.group3.iwa.back.models.LocationKafka;
import polytech.group3.iwa.back.repositories.LocationRepository;
import polytech.group3.iwa.back.repositories.UserLocationRepository;
import polytech.group3.iwa.back.repositories.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static java.lang.Math.*;

@Component
@EnableKafka
public class KafkaReceiver {

    private List<LocationKafka> locationList = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserLocationRepository userLocationRepository;

    public KafkaReceiver() {
        super();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    private void sendWarningMail(List<String> listId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("IMPORTANT - COVID ALERT");
        message.setText("Hello, you have recently added a location to our application. This location was recently frequented by a person positive to covid-19. You are now considered as a contact case. Please, be tested as soon as possible.");

        for (String s : listId) {
            String user = userRepository.findMailFromId(s);
            message.setTo(user);
            this.emailSender.send(message);
        }
    }

    public double distance(double lon1, double lat1, double lon2, double lat2) {

        return acos(sin(lat1 * PI/180.) * sin(lat2 * PI/180.) + cos(lat1 * PI/180.) * cos(lat2 * PI/180.) * cos((lon1 - lon2) * PI/180.)) * 180./PI * 60. * 1.1515 * 1.609344;
    }

    @KafkaListener(topics = "dangerous_location")
    public void receive(LocationKafka location) {
        System.out.println("localisation dangereuse reçue");
        LOGGER.info("received dangerous location='{}'", location.toString());
        int i = locationList.size() - 1;
        List<String> listId = new ArrayList<>();
        while(i >= 0 && Duration.between(LocalDateTime.parse(locationList.get(i).getLocation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), LocalDateTime.parse(location.getLocation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))).toMinutes() < 60) {
            if (distance(location.getLongitude(), location.getLatitude(), locationList.get(i).getLongitude(), locationList.get(i).getLatitude()) < 0.015) {
                if(!Objects.equals(locationList.get(i).getUserid(), location.getUserid()) && !listId.contains(locationList.get(i).getUserid())) {
                    listId.add(locationList.get(i).getUserid());
                }
            }
            i--;
        }
        sendWarningMail(listId);
        latch.countDown();
    }

    @KafkaListener(
            groupId = "location3",
            topicPartitions = @TopicPartition(
                    topic = "location",
                    partitionOffsets = { @PartitionOffset(
                            partition = "0",
                            initialOffset = "0") }))
    void listenToPartitionWithOffset(
            @Payload LocationKafka message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) int offset) {
        LOGGER.info("Received location [{}] from partition-{} with offset-{}",
                message,
                partition,
                offset);

        List<Location> listDangerous = locationRepository.findDangerousLocation(message.getLongitude(), message.getLatitude(), LocalDateTime.parse(message.getLocation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        boolean willSend = true;
        for (Location loc: listDangerous) {
            List<String> listUsers = userLocationRepository.findUsersByLocation(loc.getId_location());
            willSend = willSend && !listUsers.contains(message.getUserid());
        }

        if (listDangerous.size() != 0 && willSend) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject("IMPORTANT - COVID ALERT");
            mail.setText("Hello, you have recently added a location to our application. This location was recently frequented by a person positive to covid-19. You are now considered as a contact case. Please, be tested as soon as possible.");
            String user = userRepository.findMailFromId(message.getUserid());
            mail.setTo(user);
            this.emailSender.send(mail);
        }
        locationList.add(message);
        int i = 0;
        while(i < locationList.size() && Duration.between(LocalDateTime.parse(locationList.get(i).getLocation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), LocalDateTime.now()).toHours() > 168) {
            locationList.remove(i);
        }
        System.out.println("there are " + locationList.size() +  " locations");
        latch.countDown();
    }
}
