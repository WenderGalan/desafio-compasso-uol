package io.github.wendergalan.desafiocompassouol.config.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Validation {
    private String field;
    private String message;
}
