package alura.forumhub.api.dto.resposta.topico;

import alura.forumhub.api.domain.topico.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RespostaCriarTopico {
    private Long id;
    private String titulo;
    private String mensagem;
    private Status status;
    private Long cursoId;
    private String cursoNome;
}