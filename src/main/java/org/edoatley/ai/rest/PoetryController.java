package org.edoatley.ai.rest;

import lombok.extern.slf4j.Slf4j;

import org.edoatley.ai.rest.model.Poetry;
import org.edoatley.ai.service.poetry.PoetryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/ai/poetry")
public class PoetryController {

    private final PoetryService poetryService;

    public PoetryController(PoetryService poetryService) {
        this.poetryService = poetryService;
    }

    @GetMapping(value = "/cathaiku", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poetry> generateHaiku(){
        log.info("Haiku requested");
        Poetry haiku = poetryService.getCatHaiku();
        log.info("Haiku found: {}", haiku.getPoem());
        return ResponseEntity.ok(haiku);
    }
}
