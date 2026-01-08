package alura.forumhub.api.dto.resposta.usuario;

import alura.forumhub.api.domain.usuario.Usuario;
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
public class RespostaListarUsuarios {
    private Long id;
    private String nome;
    private String email;
    private String perfil;

    public RespostaListarUsuarios(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null;
    }
}
