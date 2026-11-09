package com.gamevault.gamevault.controller;

import com.gamevault.gamevault.model.Usuario;
import com.gamevault.gamevault.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Tela de Login
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    // Processar Login (LEITURA no Banco)
    @PostMapping("/login")
    public String realizarLogin(@ModelAttribute Usuario usuario, HttpSession session, Model model) {
            Optional<Usuario> userBanco = usuarioRepository.findByUsername(usuario.getUsername());

        if (userBanco.isPresent() && encoder.matches(usuario.getSenha(), userBanco.get().getSenha())) {
            session.setAttribute("usuarioLogado", userBanco.get());
            return "redirect:/jogos";
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos!");
            return "login";
        }
    }

    // Tela de Registo
    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Processar Registo (ESCRITA no Banco)
    @PostMapping("/registro")
    public String realizarRegistro(@ModelAttribute Usuario usuario, Model model) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            model.addAttribute("erro", "Nome de usuário já existe!");
            return "registro";
        }
        
        usuario.setId(null); // nunca aceitar id vindo do formulário (evita sobrescrever outra conta)
        usuario.setSenha(encoder.encode(usuario.getSenha())); // grava só o hash
        usuarioRepository.save(usuario);
        return "redirect:/login?sucesso";
    }

    // Sair da Conta (Logout)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}