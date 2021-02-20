package io.github.wendergalan.desafiocompassouol.api.exception;


import io.github.wendergalan.desafiocompassouol.config.model.Validation;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ApiErros {
    private List<Validation> errors;

    public ApiErros(BindingResult bindingResult) {
        List<ObjectError> erros = bindingResult.getAllErrors();
        List<Validation> lista = new ArrayList<>();
        if (!erros.isEmpty()) {
            for (ObjectError error : erros)
                lista.add(Validation.builder().field(((FieldError) error).getField()).message(error.getDefaultMessage()).build());
        }
        this.errors = lista;
    }

    public ApiErros(ResponseStatusException ex) {
        this.errors = Collections.singletonList(Validation.builder().message(ex.getReason()).build());
    }
}
