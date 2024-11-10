package org.edoatley.ai.startup;


import lombok.extern.slf4j.Slf4j;

import org.edoatley.ai.service.rag.loaders.Loader;
import org.edoatley.ai.service.rag.loaders.impl.PdfDataLoaderService;
import org.edoatley.ai.service.rag.loaders.impl.TikaDocumentDataLoaderService;
import org.edoatley.ai.service.rag.loaders.impl.WebDataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoaderCommandLineRunner implements CommandLineRunner {

    private final Loader pdfLoader;
    private final Loader webLoader;
    private final Loader otherLoader;

    public DataLoaderCommandLineRunner(PdfDataLoaderService pdfLoader, WebDataLoader webLoader, TikaDocumentDataLoaderService otherLoader) {
        this.pdfLoader = pdfLoader;
        this.webLoader = webLoader;
        this.otherLoader = otherLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        Thread pdfThread = createLoaderThread(pdfLoader);
        Thread webThread = createLoaderThread(webLoader);
        Thread otherThread = createLoaderThread(otherLoader);

        // Wait for all threads to complete
        pdfThread.join();
        webThread.join();
        otherThread.join();
    }

    private Thread createLoaderThread(Loader loader) {
        return Thread.startVirtualThread(() -> {
            try {
                log.info("Loading {} data", loader.getType());
                loader.load();
                log.info("Loading {} data completed", loader.getType());
            } catch (Exception e) {
                log.error("Error loading {} data", loader.getType(), e);
            }
        });
    }
}
