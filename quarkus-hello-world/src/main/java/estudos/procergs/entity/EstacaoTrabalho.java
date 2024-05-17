package estudos.procergs.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import estudos.procergs.infra.TipoEstacaoTrabalhoDeserializer;
import estudos.procergs.infra.TipoEstacaoTrabalhoSerializer;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "estacao_trabalho")
public class EstacaoTrabalho extends PanacheEntity {

    private String codigo;

    @Column(name = "tipo", columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    @JsonDeserialize(using = TipoEstacaoTrabalhoDeserializer.class)
    @JsonSerialize(using = TipoEstacaoTrabalhoSerializer.class)
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
