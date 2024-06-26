package estudos.procergs.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservaPesq {

    private Long idUsuario;

    private Long idEstacaoTrabalho;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private String nomeTipo;

    private Boolean cancelada;

    private Integer numeroPagina;

    private Integer tamanhoPagina;

    private String campoOrdenacao;

    private String sentidoOrdenacao;
}
