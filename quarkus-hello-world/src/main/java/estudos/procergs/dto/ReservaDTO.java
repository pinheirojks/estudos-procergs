package estudos.procergs.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import estudos.procergs.infra.serializer.LocalDateDeserializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {

    private Long id;

    private UsuarioDTO usuario;

    private EstacaoTrabalhoDTO estacaoTrabalho;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate data;

    private TipoReservaDTO tipo;

    private Boolean cancelada;
}
