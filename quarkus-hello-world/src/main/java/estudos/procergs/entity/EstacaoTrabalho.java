package estudos.procergs.entity;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "estacao_trabalho")
public class EstacaoTrabalho extends PanacheEntity {

    private String codigo;

    @Enumerated(EnumType.STRING)
    private TipoEstacaoTrabalhoEnum tipo;

    private Boolean ativo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
