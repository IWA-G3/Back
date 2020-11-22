package polytech.group3.iwa.alert_contact_case.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytech.group3.iwa.alert_contact_case.models.Location;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query(value = "select * " +
            "from locations " +
            "where CAST(ABS((acos(sin(latitude * PI()/180) * sin(?2 * PI()/180) + cos(latitude * PI()/180) * cos(?2 * PI()/180) * cos((longitude - ?1) * PI()/180)) * 180/PI()) * 60 * 1.1515 * 1.609344) as numeric) < (0.015) " +
            "and ABS(EXTRACT(EPOCH FROM (location_date - CAST(?3 as TIMESTAMP WITHOUT TIME ZONE)))) < 3600", nativeQuery = true)
    List<Location> findDangerousLocation(double longitude, double latitude, LocalDateTime timestamp);

}