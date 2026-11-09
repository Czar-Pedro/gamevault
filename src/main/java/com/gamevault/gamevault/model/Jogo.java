package com.gamevault.gamevault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jogos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String plataforma;
    private String genero;
    private String status;
    private Integer nota;
    private Integer horasJogadas;
    private String capaUrl;

    // Relacionamento no banco: Um utilizador pode ter vários jogos
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}