package estudos.procergs.infra.framework;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(value = EntityListener.class)
public abstract class AbstractEntity extends PanacheEntityBase {

    @Column(name = "data_hora_inclusao")
    private LocalDateTime dataHoraInclusao;

    @Column(name = "data_hora_alteracao")
    private LocalDateTime dataHoraAlteracao;

    @Column(name = "id_usuario_inclusao")
    private Long idUsuarioInclusao;

    @Column(name = "id_usuario_alteracao")
    private Long idUsuarioAlteracao;

    @Column(name = "ip_inclusao")
    private String ipInclusao;

    @Column(name = "ip_alteracao")
    private String ipAlteracao;
}
