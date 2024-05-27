package estudos.procergs.infra.excecao;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NaoAutorizadoExceptionMapper implements ExceptionMapper<NaoAutorizadoException> {

    @Override
    public Response toResponse(NaoAutorizadoException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
    }
}