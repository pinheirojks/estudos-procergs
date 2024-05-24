package estudos.procergs.infra.excecao;

import jakarta.ws.rs.WebApplicationException;

public class NaoPermitidoException extends WebApplicationException {

    public NaoPermitidoException(String string) {
        super(string);
    }     
}
