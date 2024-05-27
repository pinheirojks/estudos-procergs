package estudos.procergs.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservaPagina {

    private List<Reserva> reservas;

    private Long quantidadeRegistros;

    private Integer numeroPagina;

    private Integer tamanhoPagina;
}
