package org.edoatley.ai.rest;

import lombok.extern.slf4j.Slf4j;

import org.edoatley.ai.rest.model.Summary;
import org.edoatley.ai.rest.model.SummarySearch;
import org.edoatley.ai.service.summarize.SummarizationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/ai")
public class SummarizationController {

    private final SummarizationService summarizationService;

    public SummarizationController(SummarizationService summarizationService) {
        this.summarizationService = summarizationService;
    }

    @GetMapping(path = "/summarize", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Summary> summarize(@RequestParam String topic) {
        log.info("Summary requested for topic: {}", topic);
        SummarySearch summary = summarizationService.summarizeContent(topic);

        if (summary.resultFound()) {
            log.info("Summary generated for topic: {}", topic);
            return ResponseEntity.ok(summary.summary());
        } else {
            log.info("No summary found for topic: {}", topic);
            return ResponseEntity.notFound().build();
        }
    }
} 