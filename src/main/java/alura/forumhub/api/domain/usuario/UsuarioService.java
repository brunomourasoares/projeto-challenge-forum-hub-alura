package alura.forumhub.api.domain.usuario;

import alura.forumhub.api.domain.perfil.PerfilRepository;
import alura.forumhub.api.dto.resposta.usuario.*;
import alura.forumhub.api.dto.solicitacao.usuario.SolicitacaoAtualizarUsuario;
import alura.forumhub.api.infra.exception.ForbiddenException;
import alura.forumhub.api.infra.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RespostaListarUsuariosPaginado listarUsuarios(Pageable pageable) {
        verificarAutorizacaoAdmin();
        Page<RespostaListarUsuarios> usuarios = usuarioRepository.findAll(pageable).map(RespostaListarUsuarios::new);
        return RespostaListarUsuariosPaginado.from(usuarios);
    }

    public RespostaDetalharUsuario detalharUsuario(Long id) {
        verificarAutorizacao(id);
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return RespostaDetalharUsuario.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null)
                .build();
    }

    @Transactional
    public RespostaAtualizarUsuario atualizarUsuario(Long id, SolicitacaoAtualizarUsuario solicitacao) {
        verificarAutorizacao(id);
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        usuario.setNome(solicitacao.nome());
        usuario.setSenha(passwordEncoder.encode(solicitacao.senha()));
        usuarioRepository.save(usuario);
        return RespostaAtualizarUsuario.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null)
                .build();
    }

    @Transactional
    public void deletarUsuario(Long id) {
        verificarAutorizacao(id);
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public RespostaTrocarPerfil trocarPerfil(Long id, Long perfilId) {
        verificarAutorizacaoAdmin();
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        var perfilAdmin = perfilRepository.findById(perfilId).orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));
        usuario.setPerfil(perfilAdmin);
        usuarioRepository.save(usuario);
        return RespostaTrocarPerfil.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil() != null ? usuario.getPerfil().getNome() : null)
                .build();
    }

    private void verificarAutorizacao(Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        boolean isSelf = id.equals(userDetails.getId());
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isSelf && !isAdmin) {
            throw new ForbiddenException("Somente o próprio usuário ou administradores podem utilizar este recurso");
        }
    }

    private void verificarAutorizacaoAdmin() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new ForbiddenException("Somente administradores podem utilizar este recurso");
        }
    }
}
