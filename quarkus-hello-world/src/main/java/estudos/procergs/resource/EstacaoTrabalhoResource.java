package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.modelmapper.ModelMapper;

import estudos.procergs.dto.EstacaoTrabalhoDTO;
import estudos.procergs.dto.EstacaoTrabalhoPesqDTO;
import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import estudos.procergs.infra.interceptor.AutorizacaoRest;
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
@AutorizacaoRest
@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "apiKey", type = SecuritySchemeType.HTTP, scheme = "Bearer") })
@SecurityRequirement(name = "apiKey")
public class EstacaoTrabalhoResource {

    @Inject
    private EstacaoTrabalhoService estacaoTrabalhoService;

    private ModelMapper mapper = new ModelMapper();

    @GET
    @Operation(description = "Lista as estações de trabalho pesquisando por código, tipo e ativo")
    public List<EstacaoTrabalhoDTO> listar(@BeanParam EstacaoTrabalhoPesqDTO dto) {
        EstacaoTrabalho pesq = mapper.map(dto, EstacaoTrabalho.class);
        pesq.setTipo(TipoEstacaoTrabalhoEnum.parseByName(dto.getTipo()));
        return estacaoTrabalhoService.listar(pesq).stream()
                .map(u -> mapper.map(u, EstacaoTrabalhoDTO.class))
                .toList();
    }

    @GET
    @Path("{id}")
    @Operation(description = "Consulta uma estação de trabalho pelo seu ID")
    public EstacaoTrabalhoDTO consultar(@PathParam("id") Long id) {
        EstacaoTrabalho estacaoTrabalho = estacaoTrabalhoService.consultar(id);
        return mapper.map(estacaoTrabalho, EstacaoTrabalhoDTO.class);
    }

    @POST
    @Operation(description = "Cria uma nova estação de trabalho")
    public EstacaoTrabalhoDTO incluir(EstacaoTrabalhoDTO dto) {
        EstacaoTrabalho estacaoTrabalho = mapper.map(dto, EstacaoTrabalho.class);
        estacaoTrabalho = estacaoTrabalhoService.incluir(estacaoTrabalho);
        return mapper.map(estacaoTrabalho, EstacaoTrabalhoDTO.class);
    }

    @PUT
    @Path("{id}")
    @Operation(description = "Altera uma estação de trabalho")
    public EstacaoTrabalhoDTO alterar(@PathParam("id") Long id, EstacaoTrabalhoDTO dto) {
        EstacaoTrabalho estacaoTrabalho = mapper.map(dto, EstacaoTrabalho.class);
        estacaoTrabalho = estacaoTrabalhoService.alterar(id, estacaoTrabalho);
        return mapper.map(estacaoTrabalho, EstacaoTrabalhoDTO.class);
    }

    @DELETE
    @Path("{id}")
    @Operation(description = "Exclui uma estação de trabalho")
    public void excluir(@PathParam("id") Long id) {
        estacaoTrabalhoService.excluir(id);
    }
}
