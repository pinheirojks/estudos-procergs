package estudos.procergs.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDateTime dataHoraInclusao;

    private LocalDateTime dataHoraAlteracao;

    private Long idUsuarioInclusao;

    private Long idUsuarioAlteracao;

    private String ipInclusao;

    private String ipAlteracao;
}
