package estudos.procergs.infra.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    protected LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }


    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String dataString = jp.readValueAs(String.class);
        if (StringUtils.isBlank(dataString)) {
          return null;
        }
        // Completa com 0 caso a string venha com valor yyyy-m-ddT
        dataString = dataString.replaceAll("-(\\d)-", "-0$1-");
        // Remove timezone da string
        dataString = dataString.substring(0, 19);
        return LocalDateTime.parse(dataString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}       