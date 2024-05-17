package estudos.procergs.infra;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import estudos.procergs.enums.TipoEstacaoTrabalhoEnum;

public class TipoEstacaoTrabalhoDeserializer extends StdDeserializer<TipoEstacaoTrabalhoEnum> {

    protected TipoEstacaoTrabalhoDeserializer() {
        super(TipoEstacaoTrabalhoEnum.class);
    }

    @Override
    public TipoEstacaoTrabalhoEnum deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JacksonException {
        
            String dataString = jp.readValueAs(String.class);

            return TipoEstacaoTrabalhoEnum.parseByName(dataString);
    }

}
