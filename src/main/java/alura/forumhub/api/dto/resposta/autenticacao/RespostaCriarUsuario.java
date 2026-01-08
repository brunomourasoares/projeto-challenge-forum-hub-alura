package alura.forumhub.api.dto.resposta.autenticacao;

import alura.forumhub.api.domain.perfil.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RespostaCriarUsuario {
    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
}