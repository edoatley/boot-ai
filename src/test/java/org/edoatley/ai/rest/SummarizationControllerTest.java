package org.edoatley.ai.rest;

import org.edoatley.ai.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SummarizationControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldGenerateSummary(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/ai/summarize")
                .param("topic", "kafka"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("kafka"))
                .andExpect(jsonPath("$.summary").exists());
    }
} 