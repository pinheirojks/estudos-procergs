package estudos.procergs.parser;

import org.modelmapper.ModelMapper;

import estudos.procergs.dto.ReservaDTO;
import estudos.procergs.dto.ReservaPesqDTO;
import estudos.procergs.dto.TipoReservaDTO;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.enums.TipoReservaEnum;

public class ReservaMapper {

    private ModelMapper mapper = new ModelMapper();

    public ReservaDTO paraDTO(Reserva reserva) {
        ReservaDTO dto = mapper.map(reserva, ReservaDTO.class);
        dto.setTipo(new TipoReservaDTO(reserva.getTipo().name(), reserva.getTipo().getDescricao()));
        return dto;
    }

    public Reserva paraReserva(ReservaDTO dto) {
        Reserva reserva = mapper.map(dto, Reserva.class);
        reserva.setTipo(TipoReservaEnum.parseByName(dto.getTipo().getNome()));
        return reserva;
    }

    public ReservaPesq paraPesquisa(ReservaPesqDTO dto) {
        return mapper.map(dto, ReservaPesq.class);
    }
}
