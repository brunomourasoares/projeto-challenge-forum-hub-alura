package alura.forumhub.api.domain.topico;

import alura.forumhub.api.domain.curso.CursoRepository;
import alura.forumhub.api.domain.usuario.UsuarioRepository;
import alura.forumhub.api.dto.resposta.topico.RespostaAtualizarTopico;
import alura.forumhub.api.dto.resposta.topico.RespostaCriarTopico;
import alura.forumhub.api.dto.resposta.topico.RespostaDetalharTopico;
import alura.forumhub.api.dto.resposta.topico.RespostaListarTopicos;
import alura.forumhub.api.dto.resposta.topico.RespostaListarTopicosPaginado;
import alura.forumhub.api.dto.solicitacao.topico.SolicitacaoAtualizarTopico;
import alura.forumhub.api.dto.solicitacao.topico.SolicitacaoCriarTopico;
import alura.forumhub.api.infra.security.UserDetailsImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository repository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.topicoRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public RespostaCriarTopico criarTopico(SolicitacaoCriarTopico solicitacao) {
        var jaExiste = topicoRepository.existsByTitulo(solicitacao.titulo());
        if (jaExiste) {
            throw new EntityExistsException("Já existe um tópico com esse título");
        }
        var autor = usuarioRepository.findById(solicitacao.idAutor()).orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));
        var curso = cursoRepository.findById(solicitacao.idCurso()).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        var topico = new Topico(
                solicitacao.titulo(),
                solicitacao.mensagem(),
                LocalDateTime.now(),
                Status.NAO_RESPONDIDO,
                autor,
                curso
        );
        topicoRepository.save(topico);
        return RespostaCriarTopico.builder()
                .id(topico.getId())
                .titulo(topico.getTitulo())
                .mensagem(topico.getMensagem())
                .status(topico.getStatus())
                .cursoId(curso.getId())
                .cursoNome(curso.getNome())
                .build();
    }

    public RespostaListarTopicosPaginado listarTopicos(Pageable pageable) {
        Page<RespostaListarTopicos> topicos = topicoRepository.findAll(pageable).map(RespostaListarTopicos::new);
        return RespostaListarTopicosPaginado.from(topicos);
    }

    public RespostaDetalharTopico detalharTopico(Long id) {
        var topico = topicoRepository.findById(id);
        var topicoValidado = topico.orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        return RespostaDetalharTopico.builder()
                .id(topicoValidado.getId())
                .titulo(topicoValidado.getTitulo())
                .mensagem(topicoValidado.getMensagem())
                .dataCriacao(topicoValidado.getDataCriacao())
                .status(topicoValidado.getStatus())
                .autorId(topicoValidado.getAutor() != null ? topicoValidado.getAutor().getId() : null)
                .autorNome(topicoValidado.getAutor() != null ? topicoValidado.getAutor().getNome() : null)
                .cursoId(topicoValidado.getCurso() != null ? topicoValidado.getCurso().getId() : null)
                .cursoNome(topicoValidado.getCurso() != null ? topicoValidado.getCurso().getNome() : null)
                .build();
    }

    @Transactional
    public RespostaAtualizarTopico atualizarTopico(Long id, SolicitacaoAtualizarTopico solicitacao) {
        var topicoParaAtualizar = topicoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        verificarAutorizacao(topicoParaAtualizar.getAutor().getId());
        var cursoValido = cursoRepository.findById(solicitacao.idCurso()).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        topicoParaAtualizar.setTitulo(solicitacao.titulo());
        topicoParaAtualizar.setMensagem(solicitacao.mensagem());
        topicoParaAtualizar.setCurso(cursoValido);
        topicoRepository.save(topicoParaAtualizar);
        return RespostaAtualizarTopico.builder()
                .id(topicoParaAtualizar.getId())
                .titulo(topicoParaAtualizar.getTitulo())
                .mensagem(topicoParaAtualizar.getMensagem())
                .dataCriacao(topicoParaAtualizar.getDataCriacao())
                .status(topicoParaAtualizar.getStatus())
                .autorId(topicoParaAtualizar.getAutor() != null ? topicoParaAtualizar.getAutor().getId() : null)
                .autorNome(topicoParaAtualizar.getAutor() != null ? topicoParaAtualizar.getAutor().getNome() : null)
                .cursoId(topicoParaAtualizar.getCurso() != null ? topicoParaAtualizar.getCurso().getId() : null)
                .cursoNome(topicoParaAtualizar.getCurso() != null ? topicoParaAtualizar.getCurso().getNome() : null)
                .build();
    }

    @Transactional
    public void deletarTopico(Long id) {
        var topico = topicoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        verificarAutorizacao(topico.getAutor().getId());
        topicoRepository.delete(topico);
    }

    private void verificarAutorizacao(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        boolean isSelf = id.equals(userDetails.getId());
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isSelf && !isAdmin) {
            throw new SecurityException("Somente o próprio autor ou administradores podem utilizar este recurso");
        }
    }
}
