package polytech.group3.iwa.alert_contact_case;


import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import polytech.group3.iwa.alert_contact_case.controllers.UserController;
import polytech.group3.iwa.alert_contact_case.models.CovidInfo;
import polytech.group3.iwa.alert_contact_case.models.CovidInfoId;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @Test
    @DisplayName("POST /api/users/declarePositive")
    public void testAddNewContamination() throws Exception {
        String date = "2020-11-20 14:45:45";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        CovidInfo mockContamination = new CovidInfo(new CovidInfoId(1, 1, dateTime));
        doReturn(mockContamination).when(userController).newContamination(mockContamination);
        String json = asJsonString(mockContamination).substring(0,66);
        json = json + "\"2020-11-20 14:45:45\"" + "}}";
        JSONObject jsonObject = new JSONObject(json);

        mockMvc.perform(post("/api/users/declarePositive")
                .contentType("application/json")
                .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
