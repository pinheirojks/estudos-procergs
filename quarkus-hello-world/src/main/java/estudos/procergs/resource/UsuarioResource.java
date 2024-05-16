package estudos.procergs.resource;

import java.util.List;

import estudos.procergs.entity.Usuario;
import estudos.procergs.usuario.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    @GET
    public List<Usuario> listaTodos(){
        return usuarioService.listaTodos();
    }

}
