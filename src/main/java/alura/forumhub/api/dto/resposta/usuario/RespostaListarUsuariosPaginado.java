package alura.forumhub.api.dto.resposta.usuario;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RespostaListarUsuariosPaginado {

    private List<RespostaListarUsuarios> conteudo;
    private int pagina;
    private int tamanho;
    private long totalElementos;
    private int totalPaginas;
    private boolean primeira;
    private boolean ultima;
    private List<Ordenacao> ordenacao;

    public static RespostaListarUsuariosPaginado from(Page<RespostaListarUsuarios> page) {
        return RespostaListarUsuariosPaginado.builder()
                .conteudo(page.getContent())
                .pagina(page.getNumber())
                .tamanho(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .primeira(page.isFirst())
                .ultima(page.isLast())
                .ordenacao(page.getSort().stream()
                        .map(order -> new Ordenacao(order.getProperty(), order.getDirection()))
                        .toList())
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Ordenacao {
        private String propriedade;
        private Sort.Direction direcao;
    }
}
