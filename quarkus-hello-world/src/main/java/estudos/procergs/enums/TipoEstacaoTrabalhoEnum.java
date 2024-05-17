package estudos.procergs.enums;

public enum TipoEstacaoTrabalhoEnum {

    PONTO_FIXO("Ponto fixo"),
    COWORKING("Coworking"),
    NOTEBOOK("Notebook");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    private TipoEstacaoTrabalhoEnum(String descricao) {
        this.descricao = descricao;
    }

}
