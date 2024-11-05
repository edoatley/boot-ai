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
    private List<String> resources;
    private String test;

    public RagDataConfiguration() {
        resources = new ArrayList<>();
        test = "initial";
    }
}
