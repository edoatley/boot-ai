package org.edoatley.ai.rest.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Poetry {
    private final String request;
    private final String poem;
}
