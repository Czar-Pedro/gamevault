package com.gamevault.gamevault.repository;

import com.gamevault.gamevault.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // LEITURA: Procura utilizador por username e senha para efetuar Login
    Optional<Usuario> findByUsernameAndSenha(String username, String senha);
    Optional<Usuario> findByUsername(String username);
}