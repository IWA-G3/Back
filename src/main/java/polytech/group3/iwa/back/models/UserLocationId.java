package polytech.group3.iwa.back.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserLocationId implements Serializable {
    private String id_keycloak;
    private int id_location;

    public UserLocationId(String id_keycloak, int id_location) {
        this.id_keycloak = id_keycloak;
        this.id_location = id_location;
    }

    public UserLocationId() {

    }

    public String getId_keycloak() {
        return id_keycloak;
    }

    public void setId_keycloak(String id_keycloak) {
        this.id_keycloak = id_keycloak;
    }

    public int getId_location() {
        return id_location;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }
}
