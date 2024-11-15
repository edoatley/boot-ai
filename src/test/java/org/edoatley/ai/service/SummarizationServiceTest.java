package org.edoatley.ai.service;

import org.edoatley.ai.rest.model.Summary;
import org.edoatley.ai.rest.model.SummarySearch;
import org.edoatley.ai.service.chat.DataRetrievalService;
import org.edoatley.ai.service.summarize.SummarizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummarizationServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private DataRetrievalService dataRetrievalService;

    @InjectMocks
    private SummarizationService summarizationService;

    @Test
    void shouldReturnMessageWhenNoContentFound() {
        when(dataRetrievalService.searchData(anyString())).thenReturn(List.of());

        SummarySearch result = summarizationService.summarizeContent("kafka");

        assertThat(result.message()).contains("No relevant content found");
    }

    @Test
    void shouldGenerateSummaryWhenContentFound() {
        String topic = "Kafka";
        configureSummarySearchAiResult(topic, "Summary of Kafka brokers");
        SummarySearch summarySearch = new SummarySearch(true, new Summary(topic, "?", "Summary of Kafka brokers", List.of()), null);
        SummarySearch result = summarizationService.summarizeContent(topic);
        assertThat(result).isEqualTo(summarySearch);
    }

    @Test
    void shouldExtractTheThreeSummaryTypes() throws IOException {
        
        String summary = Files.readString(Paths.get("src/test/resources/data/example-summary.txt"));
        String topic = "Kafka Brokers";
        configureSummarySearchAiResult(topic, summary);

        SummarySearch result = summarizationService.summarizeContent(topic);

        String expectedShortSummary = """
                Kafka brokers, when deployed beyond a standalone setup, require careful configuration to ensure proper functionality within a cluster. One critical parameter is the `broker.id`, a unique integer identifier assigned to each broker within a Kafka cluster. While this integer is set to 0 by default, it must be unique and is recommended to be aligned with intrinsic host identifiers to simplify maintenance tasks. For instance, if hostnames contain unique numbers, these can be used for setting broker IDs.

                Another vital configuration involves `listeners`, which define the network endpoints on which a broker listens for incoming connections. The configuration has evolved from simpler port settings to a more detailed URI format that includes protocol, hostname, and port. It's crucial to ensure that the correct security protocols are mapped if non-standard listener names are used. Care must be taken when choosing ports, especially those below 1024, as running Kafka as root is not advised.

                Furthermore, the `zookeeper.connect` parameter specifies the location of the ZooKeeper ensemble used for storing broker metadata. This configuration is defined as a semicolon-separated list of hostname:port/path strings. Ensuring accurate configuration of these parameters is essential for the smooth operation of Kafka brokers in a clustered environment.""";
        assertAll(
            () -> assertThat(result.summary().topic()).isEqualTo(topic),
            () -> assertThat(result.resultFound()).isTrue(),
            () -> assertThat(result.summary().tldr()).isEqualTo("Kafka brokers require specific configuration parameters for proper operation in a clustered environment."),
            () -> assertThat(result.summary().shortSummary()).isEqualTo(expectedShortSummary),
            () -> assertThat(result.summary().bulletPoints()).containsExactly(
                "Kafka brokers require unique `broker.id` values within a cluster.",
                "`broker.id` should ideally reflect intrinsic host identifiers for easier maintenance.",
                "`listeners` configuration has transitioned from simple port settings to URI-based formats.",
                "Non-standard listener names require `listener.security.protocol.map` configuration.",
                "Ports below 1024 require Kafka to be started as root, which is not recommended.",
                "`zookeeper.connect` defines the ZooKeeper ensemble location for broker metadata storage."
            )
        );


    }

    private void configureSummarySearchAiResult(String topic, String desiredSummary) {
        Document doc = new Document("Sample Kafka content about brokers");
        when(dataRetrievalService.searchData(topic)).thenReturn(List.of(doc));
        ChatClientRequestSpec mockRequestSpec = Mockito.mock(ChatClientRequestSpec.class);
        CallResponseSpec mockCallResponseSpec = Mockito.mock(CallResponseSpec.class);

        // call sequence from ChatClient.prompt() call
        when(chatClient.prompt(anyString())).thenReturn(mockRequestSpec);
        when(mockRequestSpec.call()).thenReturn(mockCallResponseSpec);
        when(mockCallResponseSpec.content()).thenReturn(desiredSummary);
    }
}