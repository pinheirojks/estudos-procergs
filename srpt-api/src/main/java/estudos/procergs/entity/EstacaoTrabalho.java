package estudos.procergs.entity;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estacao_trabalho")
public class EstacaoTrabalho extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    private String codigo;

    @Column(name = "tipo", columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    private TipoEstacaoTrabalhoEnum tipo;

    private Boolean ativo;
}
