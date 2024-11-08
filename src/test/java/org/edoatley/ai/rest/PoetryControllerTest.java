package org.edoatley.ai.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.edoatley.ai.matcher.IsHaiku.isHaiku;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.edoatley.ai.AbstractIntegrationTest;

class PoetryControllerTest extends AbstractIntegrationTest {

    @Test
    void generateHaiku(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/ai/cathaiku"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.poem").value(isHaiku()));
    }

}