package estudos.procergs.resource;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import estudos.procergs.entity.EstacaoTrabalho;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/estacao-trabalho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Estação Trabalho", description = "CRUD de estação de trabalho")
public interface EstacaoTrabalhoResource extends PanacheEntityResource<EstacaoTrabalho,Long>{
    
}
