package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.dto.UsuarioDTO;
import estudos.procergs.dto.UsuarioPesqDTO;
import estudos.procergs.entity.Usuario;
import estudos.procergs.infra.interceptor.AutorizacaoInterceptor;
import estudos.procergs.mapper.UsuarioMapper;
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
@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários", description = "CRUD de usuários")
@AutorizacaoInterceptor
@SecuritySchemes(value = { @SecurityScheme(securitySchemeName = "apiKey",  type = SecuritySchemeType.HTTP, scheme = "Bearer") })
@SecurityRequirement(name = "apiKey")
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private UsuarioMapper usuarioMapper;

    @GET
    @Operation(description = "Lista os usuários pesquisando por login e ativo")
    public List<UsuarioDTO> listar(@BeanParam UsuarioPesqDTO dto) {
        Usuario pesq = usuarioMapper.paraUsuario(dto);        
        return usuarioService.listar(pesq).stream()
            .map(u -> usuarioMapper.paraDTO(u))
            .toList();
    }

    @GET
    @Path("{id}")
    @Operation(description = "Consulta um usuário pelo seu ID")
    public UsuarioDTO consultar(@PathParam("id") Long id) {        
        Usuario usuario = usuarioService.consultar(id);
        return usuarioMapper.paraDTO(usuario);
    }

    @POST
    @Operation(description = "Cria um novo usuário")
    public UsuarioDTO incluir(UsuarioDTO dto) {        
        Usuario usuario = usuarioMapper.paraUsuario(dto);
        usuario = usuarioService.incluir(usuario);
        return usuarioMapper.paraDTO(usuario);
    }

    @PUT
    @Path("{id}")
    @Operation(description = "Altera um usuário")
    public UsuarioDTO alterar(@PathParam("id") Long id, UsuarioDTO dto) {        
        Usuario usuario = usuarioMapper.paraUsuario(dto);
        usuario = usuarioService.alterar(id, usuario);
        return usuarioMapper.paraDTO(usuario);
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Exclui um usuário")
    public void excluir(@PathParam("id") Long id) {     
        usuarioService.excluir(id);
    }
}
