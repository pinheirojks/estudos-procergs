package estudos.procergs.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.modelmapper.ModelMapper;

import estudos.procergs.dto.ReservaDTO;
import estudos.procergs.dto.ReservaPesqDTO;
import estudos.procergs.dto.TipoReservaDTO;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.service.ReservaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reserva")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reservas", description = "CRUD de reservas de estações de trabalho")
public class ReservaResource {

    @Inject
    private ReservaService reservaService;

    private ModelMapper mapper = new ModelMapper();

    @GET
    @Operation(description = "Lista os usuários pesquisando por login e ativo")
    public List<ReservaDTO> listar(@BeanParam ReservaPesqDTO pesqDTO) {
        ReservaPesq pesq = mapper.map(pesqDTO, ReservaPesq.class);
        //pesq.setDataInicio()

        return reservaService.listar(pesq).stream()
            .map(reserva -> {
                ReservaDTO dto = mapper.map(reserva, ReservaDTO.class);
                dto.setTipo(new TipoReservaDTO(dto.getTipo().getNome(), dto.getTipo().getDescricao()));
                return dto;
            })
            .toList();
    }
}
