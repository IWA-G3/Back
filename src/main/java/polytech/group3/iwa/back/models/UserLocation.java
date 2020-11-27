package polytech.group3.iwa.back.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name="user_locations")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class UserLocation {
    @EmbeddedId
    private UserLocationId userLocationId;

    public UserLocation(UserLocationId userLocationId) {
        this.userLocationId = userLocationId;
    }

    public UserLocation() {

    }

    public UserLocationId getUserLocationId() {
        return userLocationId;
    }

    public void setUserLocationId(UserLocationId userLocationId) {
        this.userLocationId = userLocationId;
    }
}
