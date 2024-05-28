package estudos.procergs.infra.framework;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jakarta.ws.rs.WebApplicationException;

public abstract class AbstractService {

    protected void exigir(Object objeto, String mensagem) {
        if (Objects.isNull(objeto)) {
            throw new WebApplicationException(mensagem);
        }
    }

    protected void exigirString(String string, String mensagem) {
        if (StringUtils.isBlank(string)) {
            throw new WebApplicationException(mensagem);
        }
    }

    protected void proibir(Boolean condicaoProibida, String mensagem) {
        if (condicaoProibida) {
            throw new WebApplicationException(mensagem);
        }
    }
}
