package io.github.wendergalan.desafiocompassouol.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "cidade", schema = "public")
public class Cidade implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotNull
    private String nome;

    @Column(name = "estado", nullable = false)
    @NotNull
    private String estado;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
    private List<Cliente> clientes;
}
