package estudos.procergs.entity;

import java.time.LocalDate;

import javax.swing.text.StyledEditorKit.BoldAction;

import estudos.procergs.enums.TipoReservaEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "reserva")
public class Reserva extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_estacao_trabalho")
    private EstacaoTrabalho estacaoTrabalho;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", columnDefinition = "varchar(30)")
    private TipoReservaEnum tipo;

    private Boolean cancelada;

}
