package org.edoatley.ai.rest.model;

import java.util.List;

public record Summary(String topic, String tldr, String shortSummary, List<String> bulletPoints) {

}
