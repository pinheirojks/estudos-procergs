package estudos.procergs.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private Long id;

    private UsuarioDTO usuario;

    private EstacaoTrabalhoDTO estacaoTrabalho;

    private LocalDate data;

    private TipoReservaDTO tipo;

    private Boolean cancelada;
}
