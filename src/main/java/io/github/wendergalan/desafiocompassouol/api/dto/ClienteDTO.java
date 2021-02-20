package io.github.wendergalan.desafiocompassouol.api.dto;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.model.enums.Sexo;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private Sexo sexo;

    @NotNull
    private LocalDate nascimento;

    @NotNull
    private int idade;

    @NotNull
    private Cidade cidade;
}
