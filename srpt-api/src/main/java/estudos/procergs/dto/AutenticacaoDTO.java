package estudos.procergs.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutenticacaoDTO {

    @QueryParam(value = "matricula")
    private Long matricula;

    @QueryParam(value = "senha")
    private String senha;

    @QueryParam(value = "ip")
    private String ip;
}
