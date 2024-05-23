package estudos.procergs.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstacaoTrabalhoPesqDTO {

    @QueryParam(value = "codigo")
    private String codigo;

    @QueryParam(value = "ativo")
    private Boolean ativo;

    @QueryParam(value = "tipo")
    private String tipo;
}
