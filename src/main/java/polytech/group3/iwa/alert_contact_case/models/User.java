package polytech.group3.iwa.alert_contact_case.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity(name="users")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_keycloak;

    private String contact_mail;

    @ManyToMany
    @JoinTable(name="user_locations",
            joinColumns = @JoinColumn(name="id_keycloak"),
            inverseJoinColumns = @JoinColumn(name="id_location"))
    @JsonIgnore
    private List<Location> locations;

    @OneToMany(mappedBy="user")
    @JsonIgnore
    private List<CovidInfo> covidInfos;

    public int getId_keycloak() {
        return id_keycloak;
    }

    public void setId_keycloak(int id_keycloak) {
        this.id_keycloak = id_keycloak;
    }

    public String getContact_mail() {
        return contact_mail;
    }

    public void setContact_mail(String contact_mail) {
        this.contact_mail = contact_mail;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<CovidInfo> getCovidInfos() {
        return covidInfos;
    }

    public void setCovidInfos(List<CovidInfo> covidInfos) {
        this.covidInfos = covidInfos;
    }
}
