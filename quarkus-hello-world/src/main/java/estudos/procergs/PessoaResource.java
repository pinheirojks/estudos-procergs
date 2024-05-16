package estudos.procergs;

import java.util.List;

import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/pessoa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PessoaResource {
    
    @GET
    public List<Pessoa> getAll() throws Exception {
        return Pessoa.findAll(Sort.ascending("lastName")).list();
    }
}
