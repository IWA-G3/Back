package polytech.group3.iwa.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytech.group3.iwa.back.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select contact_mail " +
            "from users " +
            "where id_keycloak = ?1", nativeQuery = true)
    String findMailFromId(String id);
}
