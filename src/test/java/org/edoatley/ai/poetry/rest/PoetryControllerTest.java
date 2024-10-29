package org.edoatley.ai.poetry.rest;

import org.edoatley.ai.AbstractAIBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.edoatley.ai.IsHaiku.isHaiku;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PoetryControllerTest extends AbstractAIBootTest {

    @Test
    void generateHaiku(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/ai/cathaiku"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.poem").value(isHaiku()));
    }

}