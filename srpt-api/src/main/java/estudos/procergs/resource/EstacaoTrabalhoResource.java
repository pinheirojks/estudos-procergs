package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.dto.EstacaoTrabalhoDTO;
import estudos.procergs.dto.EstacaoTrabalhoPesqDTO;
import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.infra.interceptor.AutorizacaoInterceptor;
import estudos.procergs.mapper.EstacaoTrabalhoMapper;
import estudos.procergs.service.EstacaoTrabalhoService;
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

@Path("/estacao-trabalho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Estação Trabalho", description = "CRUD de estação de trabalho")
@AutorizacaoInterceptor
@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "apiKey", type = SecuritySchemeType.HTTP, scheme = "Bearer") })
@SecurityRequirement(name = "apiKey")
public class EstacaoTrabalhoResource {

    @Inject
    private EstacaoTrabalhoService estacaoTrabalhoService;

    @Inject
    private EstacaoTrabalhoMapper estacaoTrabalhoMapper;

    @GET
    @Operation(description = "Lista as estações de trabalho pesquisando por código, tipo e ativo")
    public List<EstacaoTrabalhoDTO> listar(@BeanParam EstacaoTrabalhoPesqDTO dto) {
        EstacaoTrabalho pesq = estacaoTrabalhoMapper.paraEstacaoTrabalho(dto);
        return estacaoTrabalhoService.listar(pesq).stream()
                .map(estacao -> estacaoTrabalhoMapper.paraDTO(estacao))
                .toList();
    }

    @GET
    @Path("{id}")
    @Operation(description = "Consulta uma estação de trabalho pelo seu ID")
    public EstacaoTrabalhoDTO consultar(@PathParam("id") Long id) {
        EstacaoTrabalho estacaoTrabalho = estacaoTrabalhoService.consultar(id);
        return estacaoTrabalhoMapper.paraDTO(estacaoTrabalho);
    }

    @POST
    @Operation(description = "Cria uma nova estação de trabalho")
    public EstacaoTrabalhoDTO incluir(EstacaoTrabalhoDTO dto) {
        EstacaoTrabalho estacaoTrabalho = estacaoTrabalhoMapper.paraEstacaoTrabalho(dto);
        estacaoTrabalho = estacaoTrabalhoService.incluir(estacaoTrabalho);
        return estacaoTrabalhoMapper.paraDTO(estacaoTrabalho);
    }

    @PUT
    @Path("{id}")
    @Operation(description = "Altera uma estação de trabalho")
    public EstacaoTrabalhoDTO alterar(@PathParam("id") Long id, EstacaoTrabalhoDTO dto) {
        EstacaoTrabalho estacaoTrabalho = estacaoTrabalhoMapper.paraEstacaoTrabalho(dto);
        estacaoTrabalho = estacaoTrabalhoService.alterar(id, estacaoTrabalho);
        return estacaoTrabalhoMapper.paraDTO(estacaoTrabalho);
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Exclui uma estação de trabalho")
    public void excluir(@PathParam("id") Long id) {
        estacaoTrabalhoService.excluir(id);
    }
}
