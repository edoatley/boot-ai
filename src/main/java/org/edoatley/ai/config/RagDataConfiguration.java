package org.edoatley.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rag.data")
public class RagDataConfiguration {
    private List<String> pdfResources;

    public RagDataConfiguration() {
        pdfResources = new ArrayList<>();
    }
}
