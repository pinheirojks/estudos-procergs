package estudos.procergs.enums;

import java.util.stream.Stream;

import estudos.procergs.infra.attributeconverter.AbstractEnumConverter;
import estudos.procergs.infra.attributeconverter.PersistableEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEstacaoTrabalhoEnum implements PersistableEnum<Integer> {

    PONTO_FIXO(1, "Ponto fixo"),
    COWORKING(2, "Coworking"),
    NOTEBOOK(3, "Notebook");
    
    private final Integer codigo;

    private final String descricao;

    public static TipoEstacaoTrabalhoEnum getInstance(Integer codigo){
        return Stream.of(TipoEstacaoTrabalhoEnum.values())
                        .filter(e -> e.codigo.equals(codigo))
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public Integer getValue() {
        return codigo;
    }
    public static class Converter extends AbstractEnumConverter<TipoEstacaoTrabalhoEnum, Integer> {
        public Converter() {
            super(TipoEstacaoTrabalhoEnum.class);
        }
    }
}
