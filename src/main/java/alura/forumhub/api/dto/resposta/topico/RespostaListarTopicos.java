package alura.forumhub.api.dto.resposta.topico;

import alura.forumhub.api.domain.topico.Status;
import alura.forumhub.api.domain.topico.Topico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RespostaListarTopicos {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private Status status;
    private String autor;
    private String curso;

    public RespostaListarTopicos(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
        this.status = topico.getStatus();
        this.autor = topico.getAutor() != null ? topico.getAutor().getNome() : null;
        this.curso = topico.getCurso() != null ? topico.getCurso().getNome() : null;
    }
}
