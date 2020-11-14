package polytech.group3.iwa.alert_contact_case.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="covid_info")
@Access(AccessType.FIELD)
@IdClass(CovidInfoPK.class)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class CovidInfo {

    @Id
    private int id_keycloak;

    @Id
    private int id_case_type;

    private LocalDateTime reporting_date;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="id_keycloak", referencedColumnName="id_keycloak")
    private User user;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="id_case_type", referencedColumnName="id_case_type")
    private CaseType case_type;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CaseType getCase_type() {
        return case_type;
    }

    public void setCase_type(CaseType case_type) {
        this.case_type = case_type;
    }

}
