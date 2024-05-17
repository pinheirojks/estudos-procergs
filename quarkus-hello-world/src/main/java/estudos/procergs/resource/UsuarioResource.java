package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.modelmapper.ModelMapper;

import estudos.procergs.dto.UsuarioDTO;
import estudos.procergs.dto.UsuarioPesqDTO;
import estudos.procergs.entity.Usuario;
import estudos.procergs.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
    @Operation(description = "Lista os usuários pesquisando por login e ativo")
    public List<UsuarioDTO> listar(@BeanParam UsuarioPesqDTO dto) {
        Usuario pesq = mapper.map(dto, Usuario.class);        
        return usuarioService.listar(pesq).stream()
            .map(u -> mapper.map(u, UsuarioDTO.class))
            .toList();
    }

    @GET
    @Path("{id}")
    @Operation(description = "Consulta um usuário pelo seu ID")
    public UsuarioDTO consultar(@PathParam("id") Long id) {        
        Usuario usuario = usuarioService.consultar(id);
        return mapper.map(usuario, UsuarioDTO.class);
    }

    @POST
    @Operation(description = "Cria um novo usuário")
    public UsuarioDTO incluir(UsuarioDTO dto) {        
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario = usuarioService.incluir(usuario);
        return mapper.map(usuario, UsuarioDTO.class);
    }

    @PUT
    @Path("{id}")
    @Operation(description = "Altera um usuário")
    public UsuarioDTO alterar(@PathParam("id") Long id, UsuarioDTO dto) {        
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario = usuarioService.alterar(id, usuario);
        return mapper.map(usuario, UsuarioDTO.class);
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Exclui um usuário")
    public void excluir(@PathParam("id") Long id) {     
        usuarioService.excluir(id);
    }
}
