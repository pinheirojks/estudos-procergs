package estudos.procergs.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {

    private Long id;

    private UsuarioDTO usuario;

    private EstacaoTrabalhoDTO estacaoTrabalho;

    private LocalDate data;

    private TipoReservaDTO tipo;
}
