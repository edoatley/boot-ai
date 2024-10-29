package org.edoatley.ai.rest;

import org.edoatley.ai.AbstractAIBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ActuatorTest extends AbstractAIBootTest {

    public static final String BOOT_ACTUATOR_V_3_JSON = "application/vnd.spring-boot.actuator.v3+json";

    @Test
    void checkInfo(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"));
    }
    @Test
    void checkHealth(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"UP\"}"));
    }

    @Test
    void checkEnv(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/env"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(BOOT_ACTUATOR_V_3_JSON)));
    }
}
