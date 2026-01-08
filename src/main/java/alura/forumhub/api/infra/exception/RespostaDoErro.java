package alura.forumhub.api.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RespostaDoErro {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
