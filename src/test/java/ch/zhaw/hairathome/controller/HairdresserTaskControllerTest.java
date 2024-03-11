package ch.zhaw.hairathome.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ch.zhaw.hairathome.model.HairdresserTask;
import ch.zhaw.hairathome.repository.HairdresserTaskRepository;
import ch.zhaw.hairathome.security.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class HairdresserTaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    HairdresserTaskRepository hairdresserTaskRepository;

    private static final String TEST_NAME = "testTask";
    private static final Double TEST_PRICE = 10.0;
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @Test
    @Order(1)
    @WithMockUser
    public void testCreateHairdresserTask() throws Exception {

        HairdresserTask hairdresserTask = new HairdresserTask(TEST_NAME, TEST_PRICE);

        var jsonBody = ow.writeValueAsString(hairdresserTask);

        mvc.perform(post("/api/hairdresserTask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Order(2)
    @WithMockUser
    public void testGetAllHairdresserTasks() throws Exception {
        var json = getAllHairdresserTasks();

        assertFalse(json.isEmpty());
        assertTrue(json.contains(TEST_NAME));
        assertTrue(json.contains(TEST_PRICE.toString()));
    }

    @Test
    @Order(3)
    @WithMockUser
    public void testHairdresserTasks() throws Exception {
        // analyse json response and delete all test data jobs
        var json = getAllHairdresserTasks();
        JsonNode jsonNode = mapper.readTree(json);
    
        if (jsonNode.isArray()) {
            for (JsonNode x : jsonNode) {
                var id = x.get("id");
                var name = x.get("name");
                if (name != null && TEST_NAME.equals(name.asText())) {
                    hairdresserTaskRepository.deleteById(id.asText());
                }
            }
        }

        // reload jobs and assert no test data
        json = getAllHairdresserTasks();
        System.out.println(json);
        assertFalse(json.contains("\"" + TEST_NAME + "\""));
    }

    private String getAllHairdresserTasks() throws Exception {
        return mvc.perform(get("/api/hairdresserTasks")
                .contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
