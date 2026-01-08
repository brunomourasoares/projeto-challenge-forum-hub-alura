package alura.forumhub.api.controller;

import alura.forumhub.api.domain.topico.TopicoService;
import alura.forumhub.api.dto.resposta.topico.RespostaAtualizarTopico;
import alura.forumhub.api.dto.resposta.topico.RespostaCriarTopico;
import alura.forumhub.api.dto.resposta.topico.RespostaDetalharTopico;
import alura.forumhub.api.dto.resposta.topico.RespostaListarTopicosPaginado;
import alura.forumhub.api.dto.solicitacao.topico.SolicitacaoAtualizarTopico;
import alura.forumhub.api.dto.solicitacao.topico.SolicitacaoCriarTopico;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topicos")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    public ResponseEntity<RespostaCriarTopico> criarTopico(@Valid @RequestBody SolicitacaoCriarTopico solicicatacao) {
        var resposta = topicoService.criarTopico(solicicatacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<RespostaListarTopicosPaginado> listarTopicos(@PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable pageable) {
        var resposta = topicoService.listarTopicos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaDetalharTopico> detalharTopico(@PathVariable Long id) {
        var resposta = topicoService.detalharTopico(id);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaAtualizarTopico> atualizarTopico(@PathVariable Long id, @Valid @RequestBody SolicitacaoAtualizarTopico solicitacao) {
        var resposta = topicoService.atualizarTopico(id, solicitacao);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTopico(@PathVariable Long id) {
        topicoService.deletarTopico(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
