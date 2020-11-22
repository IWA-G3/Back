package polytech.group3.iwa.alert_contact_case.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import polytech.group3.iwa.alert_contact_case.models.LocationKafka;
import java.util.concurrent.CountDownLatch;

@Component
@EnableKafka
class KafkaReceiver{

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "location")
    public void receive(LocationKafka location) {
        LOGGER.info("received location='{}'", location.toString());
        latch.countDown();
    }
}
