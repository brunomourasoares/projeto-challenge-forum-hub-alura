package alura.forumhub.api.controller;

import alura.forumhub.api.domain.usuario.UsuarioService;
import alura.forumhub.api.dto.resposta.usuario.*;
import alura.forumhub.api.dto.solicitacao.usuario.SolicitacaoAtualizarUsuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<RespostaListarUsuariosPaginado> listarUsuarios(@PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        var resposta = usuarioService.listarUsuarios(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaDetalharUsuario> detalharUsuario(@PathVariable Long id) {
        var resposta = usuarioService.detalharUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaAtualizarUsuario> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody SolicitacaoAtualizarUsuario solicitacao) {
        var resposta = usuarioService.atualizarUsuario(id, solicitacao);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/promover/{id}")
    public ResponseEntity<RespostaTrocarPerfil> promoverParaAdmin(@PathVariable Long id) {
        var resposta = usuarioService.trocarPerfil(id, 1L);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PutMapping("/revogar/{id}")
    public ResponseEntity<RespostaTrocarPerfil> revogarAdmin(@PathVariable Long id) {
        var resposta = usuarioService.trocarPerfil(id, 2L);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
