package org.edoatley.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class SpringAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAIApplication.class, args);
    }
}
