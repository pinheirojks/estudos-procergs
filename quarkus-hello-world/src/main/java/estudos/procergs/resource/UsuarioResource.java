package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.modelmapper.ModelMapper;

import estudos.procergs.dto.UsuarioDTO;
import estudos.procergs.entity.Usuario;
import estudos.procergs.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//http://localhost:8080/apidocs

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários", description = "CRUD de usuários")
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    private ModelMapper mapper = new ModelMapper();

    @GET
    @Operation(description = "Lista todos os usuários")
    public List<UsuarioDTO> listaTodos() {
        return usuarioService.listaTodos().stream()
            .map(u -> mapper.map(u, UsuarioDTO.class))
            .toList();
    }

    @POST
    @Operation(description = "Cria um novo usuário")
    public UsuarioDTO criar(UsuarioDTO dto) {        
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario = usuarioService.criar(usuario);
        return mapper.map(usuario, UsuarioDTO.class);
    }
}
