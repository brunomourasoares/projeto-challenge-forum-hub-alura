package alura.forumhub.api.dto.resposta.usuario;

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
public class RespostaAtualizarUsuario {
    private Long id;
    private String nome;
    private String email;
    private String perfil;
}