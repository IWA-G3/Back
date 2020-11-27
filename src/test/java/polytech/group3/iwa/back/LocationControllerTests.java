package polytech.group3.iwa.back;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
public class LocationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void dangerousLocation() throws Exception {
        this.mockMvc.perform(get("/api/locations/dangerous?userid=1&longitude=3.6956252999999997&latitude=43.402894599999996&timestamp=2004-10-19T10:23:54")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType("application/json"));
    }

    @Test
    void dangerousLocationNullValue_thenReturns400() throws Exception {
        mockMvc.perform(get("/api/locations/dangerous")).andDo(print())
      .andExpect(status().isBadRequest());
    }
}