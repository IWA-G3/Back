package polytech.group3.iwa.alert_contact_case.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="covid_info")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class CovidInfo {

    @EmbeddedId
    private CovidInfoId covidInfoId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_keycloak", insertable = false, updatable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_case_type", insertable = false, updatable = false)
    private CaseType caseType;

    protected CovidInfo() { }

    public CovidInfo(CovidInfoId covidInfoId) {
        this.covidInfoId = covidInfoId;
    }

    public CovidInfoId getCovidInfoId() {
        return covidInfoId;
    }

    public void setCovidInfoId(CovidInfoId covidInfoId) {
        this.covidInfoId = covidInfoId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }
}
