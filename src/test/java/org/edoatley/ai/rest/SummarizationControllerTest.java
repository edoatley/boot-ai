package org.edoatley.ai.rest;

import org.edoatley.ai.AbstractIntegrationTest;
import org.edoatley.ai.rest.model.Summary;
import org.edoatley.ai.rest.model.SummarySearch;
import org.edoatley.ai.service.summarize.SummarizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SummarizationControllerTest extends AbstractIntegrationTest {

    @MockBean
    SummarizationService summarizationService;

    @BeforeEach
    void init() {
        Summary summary = new Summary("kafka", "tldr", "short summary", List.of("bullet1", "bullet2"));
        SummarySearch kafkaSumamry = new SummarySearch(true, summary, null);
        when(summarizationService.summarizeContent("kafka")).thenReturn(kafkaSumamry);
    }

    @Test
    void shouldGenerateSummary(@Autowired MockMvc mvc) throws Exception {
//                  .andExpect(content().json("{\"topic\":\"kafka\",\"tldr\":\"tldr\",\"shortSummary\":\"short summary\",\"bulletPoints\":[\"bullet1\",\"bullet2\"]}"))
        mvc
                .perform(get("/api/ai/summarize")
                        .param("topic", "kafka"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.topic").value("kafka"),
                        jsonPath("$.tldr").value("tldr"),
                        jsonPath("$.shortSummary").value("short summary"),
                        jsonPath("$.bulletPoints").isArray()
                );
    }
}

