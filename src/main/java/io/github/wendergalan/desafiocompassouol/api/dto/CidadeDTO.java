package io.github.wendergalan.desafiocompassouol.api.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class CidadeDTO {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String estado;
}
