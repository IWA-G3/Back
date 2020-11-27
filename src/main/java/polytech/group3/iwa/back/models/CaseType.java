package polytech.group3.iwa.back.models;

import javax.persistence.*;
import java.util.List;

@Entity(name="case_type")
@Access(AccessType.FIELD)
public class CaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_case_type;

    private String description;

    @OneToMany(mappedBy="caseType")
    private List<CovidInfo> covidInfos;

    public int getId_case_type() {
        return id_case_type;
    }

    public void setId_case_type(int id_case_type) {
        this.id_case_type = id_case_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CovidInfo> getCovidInfos() {
        return covidInfos;
    }

    public void setCovidInfos(List<CovidInfo> covidInfos) {
        this.covidInfos = covidInfos;
    }
}
