package polytech.group3.iwa.alert_contact_case.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polytech.group3.iwa.alert_contact_case.models.CovidInfo;

public interface CovidInfoRepository extends JpaRepository<CovidInfo, Integer> {
}