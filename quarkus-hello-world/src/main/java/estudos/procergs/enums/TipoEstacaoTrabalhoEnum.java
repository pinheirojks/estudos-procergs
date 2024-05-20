package estudos.procergs.enums;

import java.util.stream.Stream;

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

    public static TipoEstacaoTrabalhoEnum parseByName(String name){
        return Stream.of(TipoEstacaoTrabalhoEnum.values())
                        .filter(v -> v.name().equals(name))
                        .findFirst()
                        .orElse(null);
    }
}
