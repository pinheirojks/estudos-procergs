package estudos.procergs.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaPesqDTO {

    @QueryParam(value = "idUsuario")
    private Long idUsuario;

    @QueryParam(value = "idEstacaoTrabalho")
    private Long idEstacaoTrabalho;

    @QueryParam(value = "dataInicio")
    private String dataInicio;

    @QueryParam(value = "dataFim")
    private String dataFim;

    @QueryParam(value = "nomeTipo")
    private String nomeTipo;

    @QueryParam(value = "cancelada")
    private Boolean cancelada;

    @DefaultValue(value = "0")
    @QueryParam(value = "numeroPagina")
    private Integer numeroPagina;

    @DefaultValue(value = "0")
    @QueryParam(value = "tamanhoPagina")
    private Integer tamanhoPagina;

    @DefaultValue("descricao")
    @QueryParam("campoOrdenacao")
    private String campoOrdenacao;

    @DefaultValue("asc")
    @QueryParam("sentidoOrdenacao")
    private String sentidoOrdenacao;
}
