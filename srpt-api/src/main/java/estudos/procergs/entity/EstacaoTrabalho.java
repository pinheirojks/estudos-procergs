package estudos.procergs.entity;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "srpt_estacao_trabalho")
public class EstacaoTrabalho extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    private String codigo;

    @Column(name = "codigo_tipo")
    @Convert(converter = TipoEstacaoTrabalhoEnum.Converter.class)
    private TipoEstacaoTrabalhoEnum tipo;

    private Boolean ativo;
}
