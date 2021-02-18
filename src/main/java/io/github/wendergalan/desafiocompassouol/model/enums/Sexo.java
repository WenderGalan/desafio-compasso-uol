package io.github.wendergalan.desafiocompassouol.model.enums;

import lombok.Getter;

@Getter
public enum Sexo {
    MASCULINO(0),
    FEMININO(1),
    OUTROS(2);

    private int value;

    Sexo(int value) {
        this.value = value;
    }
}
