package estudos.procergs.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPesqDTO {

    @QueryParam(value = "matricula")
    private Long matricula;

    @QueryParam(value = "nome")
    private String nome;

    @QueryParam(value = "ativo")
    private Boolean ativo;
}
