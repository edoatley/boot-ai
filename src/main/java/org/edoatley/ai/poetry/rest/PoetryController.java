package org.edoatley.ai.poetry.rest;

import lombok.extern.slf4j.Slf4j;
import org.edoatley.ai.poetry.model.Poetry;
import org.edoatley.ai.poetry.service.PoetryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PoetryController {

    private final PoetryService poetryService;

    public PoetryController(PoetryService poetryService) {
        this.poetryService = poetryService;
    }

    @GetMapping(value = "/ai/cathaiku", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poetry> generateHaiku(){
        log.info("Haiku requested");
        Poetry haiku = poetryService.getCatHaiku();
        log.info("Haiku found: {}", haiku.getPoem());
        return ResponseEntity.ok(haiku);
    }
}
