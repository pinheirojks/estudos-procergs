package estudos.procergs.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;

import estudos.procergs.dto.ReservaDTO;
import estudos.procergs.dto.ReservaPaginaDTO;
import estudos.procergs.dto.ReservaPesqDTO;
import estudos.procergs.dto.TipoReservaDTO;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPagina;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.enums.TipoReservaEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReservaMapper {

    private ModelMapper mapper = new ModelMapper();

    @Inject
    private UsuarioMapper usuarioMapper;

    @Inject
    private EstacaoTrabalhoMapper estacaoTrabalhoMapper;

    public ReservaDTO paraDTO(Reserva reserva) {        
        ReservaDTO dto = mapper.map(reserva, ReservaDTO.class);
        dto.setUsuario(usuarioMapper.paraDTO(reserva.getUsuario()));
        dto.setEstacaoTrabalho(estacaoTrabalhoMapper.paraDTO(reserva.getEstacaoTrabalho()));
        dto.setTipo(new TipoReservaDTO(reserva.getTipo().name(), reserva.getTipo().getDescricao()));
        return dto;
    }

    public Reserva paraReserva(ReservaDTO dto) {
        Reserva reserva = mapper.map(dto, Reserva.class);
        reserva.setUsuario(usuarioMapper.paraUsuario(dto.getUsuario()));
        reserva.setEstacaoTrabalho(estacaoTrabalhoMapper.paraEstacaoTrabalho(dto.getEstacaoTrabalho()));
        reserva.setTipo(TipoReservaEnum.parseByName(dto.getTipo().getNome()));
        return reserva;
    }

    public ReservaPesq paraPesquisa(ReservaPesqDTO dto) {
        return mapper.map(dto, ReservaPesq.class);
    }

    public ReservaPaginaDTO paraDTO(ReservaPagina pagina) {
        ReservaPaginaDTO dto = mapper.map(pagina, ReservaPaginaDTO.class);
        List<ReservaDTO> reservasDTO = pagina.getReservas().stream()
            .map(r -> this.paraDTO(r))
            .toList();
        dto.setReservas(reservasDTO);
        return dto;
    }
}
