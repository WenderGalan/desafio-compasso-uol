package io.github.wendergalan.desafiocompassouol.api.resource;

import io.github.wendergalan.desafiocompassouol.api.dto.CidadeDTO;
import io.github.wendergalan.desafiocompassouol.api.exception.ApiErros;
import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.service.CidadeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
@Api("Cidade API")
public class CidadeController {

    private final CidadeService service;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criar uma cidade")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity create(@Valid @RequestBody CidadeDTO dto, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(new ApiErros(result));

        Cidade entity = service.save(modelMapper.map(dto, Cidade.class));

        return ResponseEntity
                .created(MvcUriComponentsBuilder.fromController(getClass()).build().toUri())
                .body(modelMapper.map(entity, CidadeDTO.class));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obtém os detalhes de uma cidade.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public CidadeDTO getById(@PathVariable Long id) {
        return service
                .getById(id)
                .map(book -> modelMapper.map(book, CidadeDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta a cidade pelo o ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public void delete(@PathVariable Long id) {
        Cidade Cidade = service
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(Cidade);
    }

    @PutMapping("/{id}")
    @ApiOperation("Atualiza uma Cidade.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public CidadeDTO update(@PathVariable Long id, @Valid @RequestBody CidadeDTO dto) {
        return service
                .getById(id)
                .map(book -> {
                    book.setNome(dto.getNome());
                    book.setEstado(dto.getEstado());
                    book = service.update(book);
                    return modelMapper.map(book, CidadeDTO.class);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    @ApiOperation("Busca a cidade por nome ou estado.")
    @ApiResponses({@ApiResponse(code = 200, message = "Ok")})
    public List<CidadeDTO> findCityByNameOrState(@RequestParam(value = "nome", required = false) String nome,
                                                 @RequestParam(value = "estado", required = false) String estado) {
        if (nome == null && estado == null)
            throw new IllegalArgumentException("Por favor informe pelo menos um parâmetro para pesquisa.");
        if (nome != null)
            return service
                    .findAllByNome(nome)
                    .stream()
                    .map(item -> modelMapper.map(item, CidadeDTO.class))
                    .collect(Collectors.toList());
        else
            return service
                    .findAllByEstado(estado)
                    .stream()
                    .map(item -> modelMapper.map(item, CidadeDTO.class))
                    .collect(Collectors.toList());
    }
}
