package com.gamevault.gamevault.repository;

import com.gamevault.gamevault.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    // LEITURA: Busca no banco de dados apenas os jogos associados ao ID do utilizador logado
    List<Jogo> findByUsuarioId(Long usuarioId);
}