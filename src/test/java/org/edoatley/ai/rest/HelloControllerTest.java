package org.edoatley.ai.rest;

import org.edoatley.ai.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HelloControllerTest extends AbstractIntegrationTest {
    @Test
    void generateGreeting(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string("Hello there!"));
    }
}