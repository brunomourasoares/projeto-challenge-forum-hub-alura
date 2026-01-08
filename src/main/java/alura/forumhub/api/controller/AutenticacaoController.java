package alura.forumhub.api.controller;

import alura.forumhub.api.domain.autenticacao.AutenticacaoService;
import alura.forumhub.api.dto.resposta.autenticacao.RespostaCriarUsuario;
import alura.forumhub.api.dto.resposta.autenticacao.RespostaLogar;
import alura.forumhub.api.dto.solicitacao.autenticacao.SolicitacaoCriarUsuario;
import alura.forumhub.api.dto.solicitacao.autenticacao.SolicitacaoLogar;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints para registro e autenticação de usuários")
@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @Operation(
        summary = "Fazer login",
        description = "Autentica um usuário e retorna tokens JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário logado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example =
                        """
                        {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example =
                        """
                        {
                            "timestamp": "2026-01-01T00:00:00.052255",
                            "status": 400,
                            "error": "Requisição inválida",
                            "message": "Detalhes sobre o erro de validação"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Não autorizado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example =
                        """
                        {
                            "timestamp": "2026-01-01T00:00:00.052255",
                            "status": 401,
                            "error": "Não autorizado",
                            "message": "Detalhes sobre o erro de autenticação"
                        }
                        """
                )
            )
        ),
    })
    @PostMapping("/logar")
    public ResponseEntity<RespostaLogar> logar(
        @Valid
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados necessários para autenticar um usuário",
            required = true,
            content = @Content(
                schema = @Schema(
                    example =
                        """
                        {
                            "email" : "enderecode@email.com.br",
                            "senha" : "SenhaSegura@123"
                        }
                        """
                )
            )
        )
        SolicitacaoLogar solicicatacao) {
        var resposta = autenticacaoService.logar(solicicatacao);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário no sistema."
    )
    @ApiResponses(
        value = {
        @ApiResponse(
            responseCode = "201",
            description = "Criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example =
                        """
                        {
                            "id": 1,
                            "nome": "Nome do Usuario",
                            "email": "enderecode@email.com.br",
                            "perfil": {
                                "id": 2,
                                "nome": "ROLE_USER"
                            }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example =
                        """
                        {
                            "timestamp": "2026-01-01T00:00:00.052255",
                            "status": 400,
                            "error": "Requisição inválida",
                            "message": "Detalhes sobre o erro de validação"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example =
                        """
                        {
                            "timestamp": "2026-01-01T00:00:00.052255",
                            "status": 409,
                            "error": "Conflito",
                            "message": "Detalhes sobre o erro de conflito."
                        }
                        """
                )
            )
        ),
    })
    @PostMapping("/registrar")
    public ResponseEntity<RespostaCriarUsuario> criarUsuario(
        @Valid
        @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados necessários para criar um usuário",
            required = true,
            content = @Content(
                schema = @Schema(
                    example =
                        """
                        {
                            "nome" : "Nome do usuario",
                            "email" : "enderecode@email.com.br",
                            "senha" : "SenhaSegura@123"
                        }
                        """
                )
            )
        )
        SolicitacaoCriarUsuario solicitacao) {
        var resposta = autenticacaoService.criarUsuario(solicitacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}
