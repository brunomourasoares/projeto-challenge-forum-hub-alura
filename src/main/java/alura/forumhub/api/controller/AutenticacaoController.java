package alura.forumhub.api.controller;

import alura.forumhub.api.domain.autenticacao.AutenticacaoService;
import alura.forumhub.api.dto.resposta.autenticacao.RespostaRegistrarUsuario;
import alura.forumhub.api.dto.resposta.autenticacao.RespostaLogar;
import alura.forumhub.api.dto.solicitacao.autenticacao.SolicitacaoRegistrarUsuario;
import alura.forumhub.api.dto.solicitacao.autenticacao.SolicitacaoLogar;
import alura.forumhub.api.infra.exception.RespostaDoErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            summary = "Autenticação de um usuário",
            description = "Este endpoint realiza a autenticação de um usuário com base nas credenciais fornecidas e retorna um token JWT em caso de sucesso."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                            {
                                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                                                            }
                                                            """
                                    ),
                                    schema = @Schema(
                                            implementation = RespostaLogar.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                            {
                                                                "timestamp": "2026-01-01T00:00:00.000000",
                                                                "status": 400,
                                                                "error": "Requisição inválida",
                                                                "message": "Detalhes sobre o erro de validação"
                                                            }
                                                            """
                                    ),
                                    schema = @Schema(
                                            implementation = RespostaDoErro.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Não autorizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value =
                                                            """
                                                                    {
                                                                        "timestamp": "2026-01-01T00:00:00.000000",
                                                                        "status": 401,
                                                                        "error": "Não autorizado",
                                                                        "message": "Detalhes sobre o erro de autenticação"
                                                                    }
                                                                    """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RespostaDoErro.class
                                    )
                            )
                    ),
            })
    @PostMapping("/logar")
    public ResponseEntity<RespostaLogar> logar(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Exemplos de payload para autenticação de usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Solicitação Válida",
                                            description = "E-mail e Senha fornecidos estão de acordo com o padrão de validação do projeto.",
                                            value =
                                                    """
                                                            {
                                                                "email" : "enderecode@email.com.br",
                                                                "senha" : "SenhaSegura@123"
                                                            }
                                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Solicitação Inválida",
                                            description = "E-mail não contém um nome válido e a senha não atende aos critérios de segurança. No minimo 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.",
                                            value =
                                                    """
                                                            {
                                                                "email" : "emailinvalido.com.br",
                                                                "senha" : "123"
                                                            }
                                                            """
                                    )
                            }
                    )
            )
            SolicitacaoLogar solicicatacao) {
        var resposta = autenticacaoService.logar(solicicatacao);
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    @Operation(
            summary = "Registro de um usuário",
            description = "Este endpoint permite o registro de um novo usuário no sistema com base nas informações fornecidas e retorna os detalhes do usuário criado."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Criado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                            {
                                                                "id": 1,
                                                                "nome": "Nome do Usuario",
                                                                "email": "enderecode@email.com.br",
                                                                "perfil": "ROLE_USER"
                                                            }
                                                            """
                                    ),
                                    schema = @Schema(
                                            implementation = RespostaRegistrarUsuario.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            """
                                                    {
                                                        "timestamp": "2026-01-01T00:00:00.052255",
                                                        "status": 400,
                                                        "error": "Requisição inválida",
                                                        "message": "Detalhes sobre o erro de validação"
                                                    }
                                                    """
                                    ),
                                    schema = @Schema(
                                            implementation = RespostaDoErro.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflito",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value =
                                                    """
                                                            {
                                                                "timestamp": "2026-01-01T00:00:00.052255",
                                                                "status": 409,
                                                                "error": "Conflito",
                                                                "message": "Detalhes sobre o erro de conflito."
                                                            }
                                                            """
                                    ),
                                    schema = @Schema(
                                            implementation = RespostaDoErro.class
                                    )
                            )
                    ),
            })
    @PostMapping("/registrar")
    public ResponseEntity<RespostaRegistrarUsuario> registrarUsuario(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Exemplos de payload para registro de usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Solicitação Válida",
                                            description = "Nome, E-mail e Senha fornecidos estão de acordo com o padrão de validação do projeto.",
                                            value =
                                                    """
                                                            {
                                                                "nome" : "Nome do usuario",
                                                                "email" : "enderecode@email.com.br",
                                                                "senha" : "SenhaSegura@123"
                                                            }
                                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Solicitação Inválida",
                                            description = "Nome está vazio, E-mail não contém um nome válido e a senha não atende aos critérios de segurança. No minimo 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.",
                                            value =
                                                    """
                                                            {
                                                                "nome" : "",
                                                                "email" : "emailinvalido.com.br",
                                                                "senha" : "123"
                                                            }
                                                            """
                                    )

                            }
                    )
            )
            SolicitacaoRegistrarUsuario solicitacao) {
        var resposta = autenticacaoService.registrarUsuario(solicitacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}
