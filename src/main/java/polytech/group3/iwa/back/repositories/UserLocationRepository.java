package polytech.group3.iwa.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytech.group3.iwa.back.models.UserLocation;

import java.util.List;

public interface UserLocationRepository extends JpaRepository<UserLocation, Integer> {
    @Query(value = "SELECT id_keycloak FROM user_locations WHERE id_location = ?1", nativeQuery = true)
    List<String> findUsersByLocation(int locationId);

}
