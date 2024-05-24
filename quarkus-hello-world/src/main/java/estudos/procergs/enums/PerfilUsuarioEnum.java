package estudos.procergs.enums;

import java.util.stream.Stream;

public enum PerfilUsuarioEnum {

    ADMINISTRADOR("Administrador"),
    FUNCIONARIO("Funcionário");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    private PerfilUsuarioEnum(String descricao) {
        this.descricao = descricao;
    }

    public static PerfilUsuarioEnum parseByName(String name){
        return Stream.of(PerfilUsuarioEnum.values())
                        .filter(v -> v.name().equals(name))
                        .findFirst()
                        .orElse(null);
    }
}
