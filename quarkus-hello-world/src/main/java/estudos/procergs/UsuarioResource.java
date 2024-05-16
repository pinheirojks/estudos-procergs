package estudos.procergs;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public interface  UsuarioResource extends PanacheEntityResource<Usuario, Long>{
    @GET
    @Path("/count")
    default long count() {
        return 5990L;
    }
}
