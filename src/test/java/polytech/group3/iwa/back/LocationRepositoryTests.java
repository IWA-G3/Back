package polytech.group3.iwa.back;


import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import polytech.group3.iwa.back.models.Location;
import polytech.group3.iwa.back.repositories.LocationRepository;

import javax.sql.DataSource;
import java.util.List;

@ExtendWith({DBUnitExtension.class,
        SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class LocationRepositoryTests {

    @Autowired
    private DataSource dataSource;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Autowired
    private LocationRepository repository;

    @Test
    @DataSet("locations.yml")
    void testFindAll() {
        List<Location> locations =
                Lists.newArrayList(repository.findAll());
        Assertions.assertEquals(2, locations.size(),
                "Expected 2 locations in the database");
    }

}
