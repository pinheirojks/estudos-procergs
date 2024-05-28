package estudos.procergs.infra.interceptor;

import com.procergs.util.autentica.SessionED;

import estudos.procergs.entity.Usuario;
import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Getter
@Setter
public class AutorizacaoRepository {

    private AutorizacaoDTO autorizacao;

    private SessionED session;

    public void incluirAutorizacao(Usuario usuario, String ip) {
        this.autorizacao = new AutorizacaoDTO();
        this.autorizacao.setUsuario(usuario);
        this.autorizacao.setIp(ip);
    }

    public void incluirAutorizacao(SessionED session, String ip) {
        this.session = session;
    }
}
