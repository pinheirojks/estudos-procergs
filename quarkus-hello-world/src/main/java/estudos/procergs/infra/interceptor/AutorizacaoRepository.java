package estudos.procergs.infra.interceptor;

import estudos.procergs.entity.Usuario;
import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Getter
@Setter
public class AutorizacaoRepository {

    private AutorizacaoDTO autorizacao;

    public void incluirAutorizacao(Usuario usuario, String ip) {
        this.autorizacao = new AutorizacaoDTO();
        this.autorizacao.setIdUsuario(usuario.getId());
        this.autorizacao.setIp(ip);
    }
}
