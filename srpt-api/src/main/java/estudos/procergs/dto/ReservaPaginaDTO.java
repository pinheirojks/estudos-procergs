package estudos.procergs.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservaPaginaDTO {

    private List<ReservaDTO> reservas;

    private Long quantidadeRegistros;

    private Integer numeroPagina;

    private Integer tamanhoPagina;
}
