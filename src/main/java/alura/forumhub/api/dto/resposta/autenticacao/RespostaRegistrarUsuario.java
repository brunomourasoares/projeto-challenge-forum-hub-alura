package alura.forumhub.api.dto.resposta.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RespostaRegistrarUsuario {
    private Long id;
    private String nome;
    private String email;
    private String perfil;
}