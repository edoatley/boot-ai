package org.edoatley.ai.rag.loaders.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.edoatley.ai.config.RagDataConfiguration;
import org.edoatley.ai.rag.loaders.Loader;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PdfDataLoaderService implements Loader {

    private final RagDataConfiguration ragData;
    private final ResourceLoader loader;
    private final VectorStore vectorStore;

    public PdfDataLoaderService(RagDataConfiguration ragData, ResourceLoader loader, VectorStore vectorStore) {
        this.ragData = ragData;
        this.loader = loader;
        this.vectorStore = vectorStore;
    }

    @SneakyThrows
    public void load() {
        log.info("Loading data from PDF");
        // Note: here we read the resources from the classpath in a single thread as when you
        // read the resources in parallel I got an error with one of the files not being found.
        List<Resource> resources = this.ragData.getPdfResources().stream()
        .map(this.loader::getResource)
        .peek(r -> log.info("Found resource PDF: {}", r.getFilename()))
        .toList();

        resources.parallelStream().forEach(this::processPdfResource);
    }

    private void processPdfResource(Resource pdfResource) {
        log.info("Loading data from PDF: {}", pdfResource.getFilename());
        var pdfReader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());
        var tokenTextSplitter = new TokenTextSplitter();
        log.info("Loading vectorstore from PDF");
        this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
        log.info("Loading data from PDF Completed");
    }

    @Override
    public String getType() {
        return "pdf";
    }
}
