package estudos.procergs.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class ReservaRepository implements PanacheRepository<Reserva> {

    @Inject
    private EntityManager entityManager;

    public List<Reserva> listar(ReservaPesq pesq) {        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> criteria = builder.createQuery(Reserva.class);

        Root<Reserva> reserva = criteria.from(Reserva.class);
        Join<Reserva, Usuario> usuario = reserva.join("usuario"); 
        Join<Reserva, EstacaoTrabalho> estacao = reserva.join("estacaoTrabalho"); 

        criteria = criteria.select(reserva);

        if (Objects.nonNull(pesq.getIdUsuario())) {            
            criteria.where(builder.equal(usuario.get("id"), pesq.getIdUsuario()));
        }
        if (Objects.nonNull(pesq.getIdEstacaoTrabalho())) {            
            criteria.where(builder.equal(estacao.get("id"), pesq.getIdEstacaoTrabalho()));
        }
        if (Objects.nonNull(pesq.getNomeTipo())) {
            criteria.where(builder.equal(reserva.get("tipo"), pesq.getNomeTipo()));
        }
        if (Objects.nonNull(pesq.getDataInicio())) {
            criteria.where(builder.greaterThanOrEqualTo(reserva.get("dataInicio"), pesq.getDataInicio()));
        }
        if (Objects.nonNull(pesq.getDataFim())) {
            criteria.where(builder.lessThanOrEqualTo(reserva.get("dataFim"), pesq.getDataFim()));
        }  
        if (Objects.nonNull(pesq.getCancelada())) {
            criteria.where(builder.equal(reserva.get("cancelada"), pesq.getCancelada()));
        }  
        List<Order> ordens = new ArrayList<>();
        ordens.add(builder.asc(reserva.get("data")));
        ordens.add(builder.asc(usuario.get("login")));
        
        criteria.orderBy(ordens);
        return entityManager.createQuery(criteria).getResultList();
    }
}
