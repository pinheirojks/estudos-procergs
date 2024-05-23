package estudos.procergs.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.dto.ReservaDTO;
import estudos.procergs.dto.ReservaPaginaDTO;
import estudos.procergs.dto.ReservaPesqDTO;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPagina;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.infra.interceptor.AutorizacaoInterceptor;
import estudos.procergs.mapper.ReservaMapper;
import estudos.procergs.service.ReservaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reserva")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reservas", description = "CRUD de reservas de estações de trabalho")
@AutorizacaoInterceptor
@SecuritySchemes(value = { @SecurityScheme(securitySchemeName = "apiKey",  type = SecuritySchemeType.HTTP, scheme = "Bearer") })
@SecurityRequirement(name = "apiKey")
public class ReservaResource {

    @Inject
    private ReservaService reservaService;
    
    private ReservaMapper reservaMapper = new ReservaMapper();

    @GET
    @Operation(description = "Lista com paginação os usuários pesquisando por diversos campos")
    public ReservaPaginaDTO listar(@BeanParam ReservaPesqDTO pesqDTO) {
        ReservaPesq pesq = reservaMapper.paraPesquisa(pesqDTO);
        ReservaPagina pagina = reservaService.listar(pesq);
        return reservaMapper.paraDTO(pagina);
    }

    @GET
    @Path("{id}")
    @Operation(description = "Consulta uma reserva pelo seu ID")
    public ReservaDTO consultar(@PathParam("id") Long id) {
        Reserva reserva = reservaService.consultar(id);
        return reservaMapper.paraDTO(reserva);
    }

    @POST
    @Operation(description = "Cria uma nova reserva")
    public ReservaDTO incluir(ReservaDTO dto) {
        Reserva reserva = reservaMapper.paraReserva(dto);
        reserva = reservaService.incluir(reserva);
        return reservaMapper.paraDTO(reserva);
    }

    @PUT
    @Path("{id}")
    @Operation(description = "Altera uma reserva")
    public ReservaDTO alterar(@PathParam("id") Long id, ReservaDTO dto) {
        Reserva reserva = reservaMapper.paraReserva(dto);
        reserva = reservaService.alterar(id, reserva);
        return reservaMapper.paraDTO(reserva);
    }

    @PUT
    @Path("{id}/cancelar")
    @Operation(description = "Cancela uma reserva")
    public void cancelar(@PathParam("id") Long id) {
        reservaService.cancelar(id);
    }
}
