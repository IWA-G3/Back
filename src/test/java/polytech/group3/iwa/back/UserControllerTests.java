package polytech.group3.iwa.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/users/declarePositive")
    public void testAddNewContamination() throws Exception {
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZF9rZXljbG9hayI6MSwianRpIjoiMWVhYzU5MjQtYTQzZS00OTM2LWE5MzYtODc5M2Y3ZmMzOGMxIiwiaWF0IjoxNjA2MDU0ODA2LCJleHAiOjE2MDYwNTg0MDZ9.IA0pN6KRe2iIV6xBwgvv2gjRSgmjF-d-4RNr60mA27g";

        mockMvc.perform(post("/api/users/declarePositive")
                .header("Authorization", jwt)
                .contentType("application/json").content("test"))
                .andExpect(status().isOk());
    }

}
