package org.edoatley.ai;

import org.edoatley.ai.poetry.model.Poetry;
import org.edoatley.ai.poetry.service.PoetryService;
import org.edoatley.ai.poetry.service.PoetryServiceImpl;
import org.edoatley.ai.rag.loaders.impl.PdfDataLoaderService;
import org.edoatley.ai.rag.loaders.impl.TikaDocumentDataLoaderService;
import org.edoatley.ai.rag.loaders.impl.WebDataLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import redis.clients.jedis.JedisSocketFactory;

@MockBeans({
    @MockBean(WebDataLoader.class),
    @MockBean(PdfDataLoaderService.class),
    @MockBean(TikaDocumentDataLoaderService.class),
    @MockBean(ChatModel.class),
    @MockBean(ChatClient.class),
    @MockBean(VectorStore.class),
    @MockBean(JedisSocketFactory.class)
})
@SpringBootTest(classes = {
    AbstractIntegrationTest.TestConfig.class
})
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @TestConfiguration
    static class TestConfig {
    @Bean
    @Primary
    public PoetryService testPoetryService() {
        return () -> new Poetry(
                PoetryServiceImpl.WRITE_ME_HAIKU_ABOUT_CAT,
                "Cats are cute and sweet\nLoving and Playful, like me!\nBut, Dogs are better!");
    }
    }
}
