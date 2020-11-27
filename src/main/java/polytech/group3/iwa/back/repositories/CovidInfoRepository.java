package polytech.group3.iwa.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polytech.group3.iwa.back.models.CovidInfo;

public interface CovidInfoRepository extends JpaRepository<CovidInfo, Integer> {
}