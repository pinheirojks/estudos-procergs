package estudos.procergs.infra.interceptor;

import com.procergs.util.autentica.SessionED;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Getter
@Setter
public class AutorizacaoRepository {

    private AutorizacaoDTO autorizacao;

    private SessionED session;

    public void incluirAutorizacao(AutorizacaoDTO autorizacao) {
        this.autorizacao = autorizacao;
    }

    public void incluirAutorizacao(SessionED session, String ip) {
        this.session = session;
    }
}
