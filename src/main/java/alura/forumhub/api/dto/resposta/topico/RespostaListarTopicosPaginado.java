package alura.forumhub.api.dto.resposta.topico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RespostaListarTopicosPaginado {

    private List<RespostaListarTopicos> conteudo;
    private int pagina;
    private int tamanho;
    private long totalElementos;
    private int totalPaginas;
    private boolean primeira;
    private boolean ultima;
    private List<Ordenacao> ordenacao;

    public static RespostaListarTopicosPaginado from(Page<RespostaListarTopicos> page) {
        return RespostaListarTopicosPaginado.builder()
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

