package estudos.procergs.dto;

public class EstacaoTrabalhoDTO {

    private Long id;

    private String codigo;

    private TipoEstacaoTrabalhoDTO tipo;

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

    public TipoEstacaoTrabalhoDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstacaoTrabalhoDTO tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
