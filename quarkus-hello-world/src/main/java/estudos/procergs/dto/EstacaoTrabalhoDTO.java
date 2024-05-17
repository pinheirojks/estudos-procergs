package estudos.procergs.dto;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class EstacaoTrabalhoDTO {

    private Long id;

    private String codigo;

    @Enumerated(EnumType.STRING)
    private TipoEstacaoTrabalhoEnum tipo;

    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoEstacaoTrabalhoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstacaoTrabalhoEnum tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
