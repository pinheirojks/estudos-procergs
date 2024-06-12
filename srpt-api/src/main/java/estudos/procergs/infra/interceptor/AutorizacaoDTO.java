package estudos.procergs.infra.interceptor;

import estudos.procergs.entity.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorizacaoDTO {
    
    private Usuario usuario;

    private String ip;

    private String token;
}
