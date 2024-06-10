package estudos.procergs.mapper;

import java.util.Objects;

import org.modelmapper.ModelMapper;

import estudos.procergs.dto.EstacaoTrabalhoDTO;
import estudos.procergs.dto.EstacaoTrabalhoPesqDTO;
import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstacaoTrabalhoMapper {

  private ModelMapper mapper = new ModelMapper();

    public EstacaoTrabalhoDTO paraDTO(EstacaoTrabalho estacao) {
        EstacaoTrabalhoDTO dto = mapper.map(estacao, EstacaoTrabalhoDTO.class);
        return dto;
    }

    public EstacaoTrabalho paraEstacaoTrabalho(EstacaoTrabalhoDTO dto) {
        EstacaoTrabalho estacao = mapper.map(dto, EstacaoTrabalho.class);
        if (Objects.nonNull(dto.getTipo())) {
            estacao.setTipo(TipoEstacaoTrabalhoEnum.getInstance(dto.getTipo().getCodigo()));
        }
        return estacao;
    }

    public EstacaoTrabalho paraEstacaoTrabalho(EstacaoTrabalhoPesqDTO dto) {
        EstacaoTrabalho estacao = mapper.map(dto, EstacaoTrabalho.class);
        if (Objects.nonNull(dto.getCodigoTipo())) {
            estacao.setTipo(TipoEstacaoTrabalhoEnum.getInstance(dto.getCodigoTipo()));
        }
        return estacao;
    }
}
