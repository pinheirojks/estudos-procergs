package estudos.procergs.entity;

import estudos.procergs.enums.PerfilUsuarioEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "srpt_usuario")
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    private Long matricula;

    private String nome;

    private String senha;

    @Column(name = "sigla_setor")
    private String siglaSetor;

    @Column(name = "perfil", columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    private PerfilUsuarioEnum perfil;

    private Boolean ativo;
}
