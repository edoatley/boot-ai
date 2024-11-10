package org.edoatley.ai.service.rag.loaders.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.edoatley.ai.config.RagDataConfiguration;
import org.edoatley.ai.service.rag.loaders.Loader;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WebDataLoader implements Loader {
    private final RagDataConfiguration ragData;
    private final VectorStore vectorStore;
    private final TextSplitter textSplitter;

    public WebDataLoader(RagDataConfiguration ragData, VectorStore vectorStore, TextSplitter textSplitter) {
        this.ragData = ragData;
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
    }

    @SneakyThrows
    @Override
    public void load() {
        List<Document> documents = new ArrayList<>();
        log.info("Loading .html files as Documents");
        for (String url : this.ragData.getWebResources()) {
            log.info("Reading URI {}", url);
            var documentUri = URI.create(url);
            var htmlReader = new TikaDocumentReader(new UrlResource(documentUri));
            documents.addAll(htmlReader.get());

            log.info("Creating and storing Embeddings from Documents");
            vectorStore.add(textSplitter.split(documents));
        }
    }

    @Override
    public String getType() {
        return "web";
    }
}
