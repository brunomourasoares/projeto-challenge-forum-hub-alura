package alura.forumhub.api.dto.solicitacao.topico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoCriarTopico(
        @NotBlank(message = "O título é obrigatório")
        String titulo,
        @NotBlank(message = "A mensagem é obrigatória")
        String mensagem,
        @NotNull(message = "O ID do autor é obrigatório")
        @Min(1)
        Long idAutor,
        @NotNull(message = "O ID do curso é obrigatório")
        @Min(1)
        Long idCurso
) {
}
