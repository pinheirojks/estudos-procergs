package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.dto.ReservaDTO;
import estudos.procergs.dto.ReservaPesqDTO;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.parser.ReservaMapper;
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
public class ReservaResource {

    @Inject
    private ReservaService reservaService;

    private ReservaMapper reservaMapper = new ReservaMapper();

    @GET
    @Operation(description = "Lista os usuários pesquisando por login e ativo")
    public List<ReservaDTO> listar(@BeanParam ReservaPesqDTO pesqDTO) {
        ReservaPesq pesq = reservaMapper.paraPesquisa(pesqDTO);
        return reservaService.listar(pesq).stream()
                .map(reserva -> reservaMapper.paraDTO(reserva))
                .toList();
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
