package estudos.procergs.infra.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;

public class TipoEstacaoTrabalhoSerializer extends StdScalarSerializer<TipoEstacaoTrabalhoEnum>{

    protected TipoEstacaoTrabalhoSerializer(){
        super(TipoEstacaoTrabalhoEnum.class);
    }

    @Override
    public void serialize(TipoEstacaoTrabalhoEnum value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(value != null ? value.name() : null);
    }

}
