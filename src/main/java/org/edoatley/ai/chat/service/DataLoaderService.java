package org.edoatley.ai.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.edoatley.ai.config.RagDataConfiguration;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DataLoaderService implements CommandLineRunner {

    private final RagDataConfiguration ragData;
    private final ResourceLoader loader;
    private final VectorStore vectorStore;

    @Autowired
    public DataLoaderService(RagDataConfiguration ragData, ResourceLoader loader, VectorStore vectorStore) {
        this.ragData = ragData;
        this.loader = loader;
        this.vectorStore = vectorStore;
    }

    public void run(String... args) {
        for (var pdfName : this.ragData.getPdfResources()) {
            Resource pdf = this.loader.getResource(pdfName);
            processPdfResource(pdf);
        }
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
}
