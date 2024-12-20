package org.edoatley.ai.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.edoatley.ai.AbstractIntegrationTest;

class ActuatorTest extends AbstractIntegrationTest {

    public static final String BOOT_ACTUATOR_V_3_JSON = "application/vnd.spring-boot.actuator.v3+json";

    @Test
    void checkInfo(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/info"))
                .andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.parseMediaType(BOOT_ACTUATOR_V_3_JSON)),
                    content().string("{}")
                );
    }

    @Test
    void checkEnv(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/env"))
                .andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.parseMediaType(BOOT_ACTUATOR_V_3_JSON))
                );
    }
}
