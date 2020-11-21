package polytech.group3.iwa.alert_contact_case.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CovidInfoPK implements Serializable {

    private int id_keycloak;

    private int id_case_type;

    public int getId_keycloak() {
        return id_keycloak;
    }

    public void setId_keycloak(int id_keycloak) {
        this.id_keycloak = id_keycloak;
    }

    public int getId_case_type() {
        return id_case_type;
    }

    public void setId_case_type(int id_case_type) {
        this.id_case_type = id_case_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CovidInfoPK that = (CovidInfoPK) o;
        return Objects.equals(id_keycloak, that.id_keycloak) &&
                Objects.equals(id_case_type, that.id_case_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_keycloak, id_case_type);
    }

}
