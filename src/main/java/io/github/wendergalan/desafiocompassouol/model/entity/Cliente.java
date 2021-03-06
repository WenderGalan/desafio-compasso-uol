package io.github.wendergalan.desafiocompassouol.model.entity;

import io.github.wendergalan.desafiocompassouol.model.enums.Sexo;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@With
@Entity
@Table(name = "cliente", schema = "public")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotNull
    private String nome;

    @Column(name = "sexo", nullable = false)
    @NotNull
    private Sexo sexo;

    @Column(name = "nascimento", nullable = false)
    @NotNull
    private LocalDate nascimento;

    @Column(name = "idade", nullable = false)
    @NotNull
    private int idade;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = false)
    private Cidade cidade;
}
