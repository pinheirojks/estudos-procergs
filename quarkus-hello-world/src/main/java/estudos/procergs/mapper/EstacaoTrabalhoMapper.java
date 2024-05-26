package estudos.procergs.mapper;

import org.modelmapper.ModelMapper;

import estudos.procergs.dto.EstacaoTrabalhoDTO;
import estudos.procergs.dto.EstacaoTrabalhoPesqDTO;
import estudos.procergs.dto.TipoEstacaoTrabalhoDTO;
import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EstacaoTrabalhoMapper {

  private ModelMapper mapper = new ModelMapper();

    public EstacaoTrabalhoDTO paraDTO(EstacaoTrabalho estacao) {
        EstacaoTrabalhoDTO dto = mapper.map(estacao, EstacaoTrabalhoDTO.class);
        dto.setTipo(new TipoEstacaoTrabalhoDTO(estacao.getTipo().name(), estacao.getTipo().getDescricao()));
        return dto;
    }

    public EstacaoTrabalho paraEstacaoTrabalho(EstacaoTrabalhoDTO dto) {
        EstacaoTrabalho estacao = mapper.map(dto, EstacaoTrabalho.class);
        estacao.setTipo(TipoEstacaoTrabalhoEnum.parseByName(dto.getTipo().getNome()));
        return estacao;
    }

    public EstacaoTrabalho paraEstacaoTrabalho(EstacaoTrabalhoPesqDTO dto) {
        EstacaoTrabalho estacao = mapper.map(dto, EstacaoTrabalho.class);
        return estacao;
    }
}
