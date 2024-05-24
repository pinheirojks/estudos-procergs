package estudos.procergs.infra.excecao;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NaoPermitidoExceptionMapper implements ExceptionMapper<NaoPermitidoException> {

    @Override
    public Response toResponse(NaoPermitidoException exception) {
        return Response.status(Response.Status.FORBIDDEN).entity(exception.getMessage()).build();
    }
}
