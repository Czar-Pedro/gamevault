package com.gamevault.gamevault.controller;

import com.gamevault.gamevault.model.Jogo;
import com.gamevault.gamevault.model.Usuario;
import com.gamevault.gamevault.repository.JogoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class JogoController {

    private final JogoRepository repository;

    public JogoController(JogoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        return "redirect:/jogos";
    }

    // TELA 1: BIBLIOTECA DO UTILIZADOR LOGADO
    @GetMapping("/jogos")
    public String biblioteca(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) return "redirect:/login";

        // Lê do banco apenas os jogos deste utilizador
        model.addAttribute("listaJogos", repository.findByUsuarioId(usuarioLogado.getId()));
        model.addAttribute("novoJogo", new Jogo());
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "index";
    }

    // TELA 2: PERFIL DO UTILIZADOR LOGADO
    @GetMapping("/jogos/perfil")
    public String perfil(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) return "redirect:/login";

        List<Jogo> todos = repository.findByUsuarioId(usuarioLogado.getId());

        int totalJogos = todos.size();
        int totalZerados = (int) todos.stream().filter(j -> "Zerado".equalsIgnoreCase(j.getStatus())).count();
        int totalHoras = todos.stream().mapToInt(j -> j.getHorasJogadas() != null ? j.getHorasJogadas() : 0).sum();
        
        double mediaNotas = todos.stream()
                .mapToInt(j -> j.getNota() != null ? j.getNota() : 0)
                .average()
                .orElse(0.0);

        model.addAttribute("totalJogos", totalJogos);
        model.addAttribute("totalZerados", totalZerados);
        model.addAttribute("totalHoras", totalHoras);
        model.addAttribute("mediaNotas", String.format("%.1f", mediaNotas));
        model.addAttribute("usuarioLogado", usuarioLogado);

        return "perfil";
    }

    // ESCRITA: CADASTRO VINCULADO AO UTILIZADOR
    @PostMapping("/jogos")
    public String salvarJogo(@ModelAttribute("novoJogo") Jogo jogo, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) return "redirect:/login";

        if (jogo.getCapaUrl() == null || jogo.getCapaUrl().isBlank()) {
            jogo.setCapaUrl("https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=500&q=80");
        }
        
        jogo.setUsuario(usuarioLogado); // Associa o jogo ao utilizador conectado
        repository.save(jogo);
        return "redirect:/jogos";
    }

    // ESCRITA: REMOÇÃO
    @GetMapping("/jogos/eliminar/{id}")
    public String eliminarJogo(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";
        repository.deleteById(id);
        return "redirect:/jogos";
    }
}