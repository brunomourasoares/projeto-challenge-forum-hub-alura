package alura.forumhub.api.domain.autenticacao;

import alura.forumhub.api.domain.perfil.PerfilRepository;
import alura.forumhub.api.domain.usuario.Usuario;
import alura.forumhub.api.domain.usuario.UsuarioRepository;
import alura.forumhub.api.dto.resposta.autenticacao.RespostaCriarUsuario;
import alura.forumhub.api.dto.resposta.autenticacao.RespostaLogar;
import alura.forumhub.api.dto.solicitacao.autenticacao.SolicitacaoCriarUsuario;
import alura.forumhub.api.dto.solicitacao.autenticacao.SolicitacaoLogar;
import alura.forumhub.api.infra.exception.UnauthorizedException;
import alura.forumhub.api.infra.security.JwtUtils;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AutenticacaoService(UsuarioRepository usuarioRepository,
                               PerfilRepository perfilRepository,
                               AuthenticationManager authenticationManager,
                               PasswordEncoder passwordEncoder,
                               JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public RespostaLogar logar(SolicitacaoLogar solicitacao) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(solicitacao.email(), solicitacao.senha())
            );
            String token = jwtUtils.generateJwtToken(authentication);
            return new RespostaLogar(token);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("E-mail ou senha incorretos.");
        }
    }

    @Transactional
    public RespostaCriarUsuario criarUsuario(SolicitacaoCriarUsuario solicitacao) {
        var jaExiste = usuarioRepository.existsByEmail(solicitacao.email());
        if (jaExiste) {
            throw new EntityExistsException("Já existe um usuário com esse e-mail!");
        }
        var usuario = new Usuario();
        BeanUtils.copyProperties(solicitacao, usuario);
        usuario.setSenha(passwordEncoder.encode(solicitacao.senha()));
        var roleUser = perfilRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Perfil ROLE_USER não encontrado."));
        usuario.setPerfil(roleUser);
        usuarioRepository.save(usuario);
        var resposta = new RespostaCriarUsuario();
        BeanUtils.copyProperties(usuario, resposta);
        return resposta;
    }
}