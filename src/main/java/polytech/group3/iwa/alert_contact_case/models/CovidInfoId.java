package polytech.group3.iwa.alert_contact_case.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class CovidInfoId implements Serializable {
    private int id_keycloak;
    private int id_case_type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reporting_date;

    public CovidInfoId(int id_keycloak, int id_case_type, LocalDateTime reporting_date) {
        this.id_keycloak = id_keycloak;
        this.id_case_type = id_case_type;
        this.reporting_date = reporting_date;
    }

    public CovidInfoId() { }

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

    public LocalDateTime getReporting_date() {
        return reporting_date;
    }

    public void setReporting_date(LocalDateTime reporting_date) {
        this.reporting_date = reporting_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CovidInfoId that = (CovidInfoId) o;
        return id_keycloak == that.id_keycloak &&
                id_case_type == that.id_case_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_keycloak, id_case_type);
    }

}
