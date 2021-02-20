package io.github.wendergalan.desafiocompassouol.api.resource;

import io.github.wendergalan.desafiocompassouol.api.dto.ClienteDTO;
import io.github.wendergalan.desafiocompassouol.api.exception.ApiErros;
import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;
import io.github.wendergalan.desafiocompassouol.service.ClienteService;
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
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Api("Cliente API")
public class ClienteController {

    private final ClienteService service;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criar um cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity create(@Valid @RequestBody ClienteDTO dto, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(new ApiErros(result));

        Cliente entity = service.save(modelMapper.map(dto, Cliente.class));

        return ResponseEntity
                .created(MvcUriComponentsBuilder.fromController(getClass()).build().toUri())
                .body(modelMapper.map(entity, ClienteDTO.class));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obtém os detalhes do cliente.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ClienteDTO getById(@PathVariable Long id) {
        return service
                .getById(id)
                .map(book -> modelMapper.map(book, ClienteDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta o cliente pelo o ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public void delete(@PathVariable Long id) {
        Cliente cliente = service
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(cliente);
    }

    @PutMapping("/{id}")
    @ApiOperation("Atualiza um cliente (Só atualiza o nome e a idade).")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ClienteDTO update(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return service
                .getById(id)
                .map(book -> {
                    book.setNome(dto.getNome());
                    book.setIdade(dto.getIdade());
                    book = service.update(book);
                    return modelMapper.map(book, ClienteDTO.class);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    @ApiOperation("Busca o cliente por nome")
    @ApiResponses({@ApiResponse(code = 200, message = "Ok")})
    public List<ClienteDTO> findClientByName(@RequestParam("nome") String nome) {
        return service.findAllByNome(nome)
                .stream()
                .map(item -> modelMapper.map(item, ClienteDTO.class))
                .collect(Collectors.toList());
    }
}
