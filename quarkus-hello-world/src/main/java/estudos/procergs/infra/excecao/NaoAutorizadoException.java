package estudos.procergs.infra.excecao;

import jakarta.ws.rs.WebApplicationException;

public class NaoAutorizadoException extends WebApplicationException {

    public NaoAutorizadoException(String string) {
        super(string);
    }
     
}
