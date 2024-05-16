package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.entity.Usuario;
import estudos.procergs.usuario.UsuarioService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//http://localhost:8080/apidocs

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários", description = "CRUD de usuários")
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    @GET
    @Operation(description = "Lista todos os usuários")
    public List<Usuario> listaTodos(){
        return usuarioService.listaTodos();
    }

    @POST
    public Response criarNovo(Usuario u) {
        return Response.ok(usuarioService.criar(u)).status(200).build();

    }

}
