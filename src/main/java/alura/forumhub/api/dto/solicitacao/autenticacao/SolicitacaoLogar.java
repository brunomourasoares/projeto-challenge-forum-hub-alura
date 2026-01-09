package alura.forumhub.api.dto.solicitacao.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SolicitacaoLogar(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @Size(min = 6 ,max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
        )
        String senha) {
}
