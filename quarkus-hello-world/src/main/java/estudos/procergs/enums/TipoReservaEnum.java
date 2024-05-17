package estudos.procergs.enums;

import java.util.stream.Stream;

public enum TipoReservaEnum {

    INTEGRAL("Integral"),
    MANHA("Manhã"),
    TARDE("Tarde");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    private TipoReservaEnum(String descricao) {
        this.descricao = descricao;
    }

    public static TipoReservaEnum parseByName(String name) {
        return Stream.of(TipoReservaEnum.values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElse(null);
    }

}
