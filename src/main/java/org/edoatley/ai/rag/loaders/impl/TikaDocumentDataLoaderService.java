package org.edoatley.ai.rag.loaders.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.edoatley.ai.config.RagDataConfiguration;
import org.edoatley.ai.rag.loaders.Loader;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TikaDocumentDataLoaderService implements Loader {

    private final RagDataConfiguration ragData;
    private final ResourceLoader loader;
    private final VectorStore vectorStore;
    private final TextSplitter textSplitter;

    public TikaDocumentDataLoaderService(RagDataConfiguration ragData, ResourceLoader loader, VectorStore vectorStore, TextSplitter textSplitter) {
        this.ragData = ragData;
        this.loader = loader;
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
    }

    @SneakyThrows
    public void load() {
        log.info("Processing documents with Apache Tika");
        processDocuments(this.ragData.getOtherResources());
    }

    private void processDocuments(List<String> wordResources) throws IOException {
        log.info("Processing documents");
        List<Resource> resources = wordResources.stream()
                .map(this.loader::getResource)
                .peek(d -> log.info("Processing Document {}", d.getFilename()))
                .toList();
        List<Document> documents = processResources(resources);
        log.info("Word Documents processed");
        vectorStore.accept(textSplitter.apply(documents));
        log.info("Word Documents added to VectorStore");
    }

    private List<Document> processResources(List<Resource> resources) throws IOException {
        List<Document> documentList = new ArrayList<>();
        resources.forEach(doc -> {
            documentList.addAll(new TikaDocumentReader(doc).get());
        });
        return documentList;
    }
    
    @Override
    public String getType() {
        return "other";
    }
}


