package estudos.procergs.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.dto.AutenticacaoDTO;
import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import estudos.procergs.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/autenticacao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticação", description = "Serviço de autenticação de usuários")
public class AutenticacaoResource {

    @Inject
    private UsuarioService usuarioService;

    @GET
    @Operation(description = "Efetuar a autenticação")
    public AutorizacaoDTO autenticar(@BeanParam AutenticacaoDTO dto) { 
        return usuarioService.autenticar(dto);
    }
}
