package estudos.procergs.dto;

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
}
