package alura.forumhub.api.infra.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RespostaDoErro> erro404(RuntimeException ex) {
        var erro = new RespostaDoErro(
                LocalDateTime.now(),
                404,
                "Não Encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<RespostaDoErro> erroNoToken(TokenException ex) {
        var erro = new RespostaDoErro(
                LocalDateTime.now(),
                401,
                "Erro no Token",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<RespostaDoErro> erroDePermissao(ForbiddenException ex) {
        var erro = new RespostaDoErro(
                LocalDateTime.now(),
                403,
                "Acesso Negado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaDoErro> erroLogin(MethodArgumentNotValidException ex) {
        String detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" | "));
        var erro = new RespostaDoErro(
                LocalDateTime.now(),
                400,
                "Requisição inválida",
                detalhes
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<RespostaDoErro> erroDeLogin(UnauthorizedException ex) {
        var erro = new RespostaDoErro(
                LocalDateTime.now(),
                401,
                "Credenciais inválidas",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<RespostaDoErro> erroDeConflito(EntityExistsException ex) {
        var erro = new RespostaDoErro(
                LocalDateTime.now(),
                409,
                "Conflito",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}
