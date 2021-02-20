package io.github.wendergalan.desafiocompassouol.config.exception;

import io.github.wendergalan.desafiocompassouol.api.exception.ApiErros;
import io.github.wendergalan.desafiocompassouol.config.model.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Global exception configuration.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionConfiguration {

    /**
     * Handle error generic response entity.
     *
     * @param ex         the ex
     * @param webRequest the web request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleErrorGeneric(Exception ex, WebRequest webRequest) {
        // Do Anything with webRequest
        if (ex instanceof ResponseStatusException)
            return new ResponseEntity(new ApiErros((ResponseStatusException) ex), ((ResponseStatusException) ex).getStatus());
        return ResponseEntity.badRequest().body(new ResponseError("Houve um problema no servidor. Por favor tente novamente mais tarde."));
    }
}