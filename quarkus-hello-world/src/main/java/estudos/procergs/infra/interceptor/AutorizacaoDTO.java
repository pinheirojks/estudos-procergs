package estudos.procergs.infra.interceptor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorizacaoDTO {
    
    private Long idUsuario;

    private String ip;
}
