package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.infra.interceptor.AutorizacaoInterceptor;
import estudos.procergs.integration.soe.UsuarioSoe;
import estudos.procergs.integration.soe.UsuarioSoeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/usuario-soe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários do SOE", description = "Serviço de usuários do SOE")
@AutorizacaoInterceptor
@SecuritySchemes(value = { @SecurityScheme(securitySchemeName = "apiKey",  type = SecuritySchemeType.HTTP, scheme = "Bearer") })
@SecurityRequirement(name = "apiKey")
public class UsuarioSoeResource {

    @Inject
    private UsuarioSoeService usuarioSoeService;

    @GET
    @Operation(description = "Lista os usuários pesquisando por orgão, matricula ou nome")
    public List<UsuarioSoe> listar(
        @QueryParam(value = "siglaOrgao") String siglaOrgao, 
        @QueryParam(value = "matricula") Long matricula, 
        @QueryParam(value = "nome") String nome) {

        return usuarioSoeService.listar(siglaOrgao, matricula, nome, null);
    }

    @GET
    @Path("{codigoUsuario}")
    @Operation(description = "Consulta um usuário pelo seu código")
    public UsuarioSoe consultar(@PathParam(value = "codigoUsuario") Long codigoUsuario) {            
        return usuarioSoeService.consultar(codigoUsuario);
    }
}
