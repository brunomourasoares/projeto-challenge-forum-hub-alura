package alura.forumhub.api.dto.resposta.topico;

import alura.forumhub.api.domain.topico.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RespostaAtualizarTopico {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private Status status;
    private Long autorId;
    private String autorNome;
    private Long cursoId;
    private String cursoNome;
}