package estudos.procergs.repository;

import java.util.List;

import estudos.procergs.entity.EstacaoTrabalho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class EstacaoTrabalhoRepository implements PanacheRepository<EstacaoTrabalho> {

    @Inject
    private EntityManager entityManager;

    public List<EstacaoTrabalho> listar(EstacaoTrabalho pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EstacaoTrabalho> criteria = builder.createQuery(EstacaoTrabalho.class);
        Root<EstacaoTrabalho> estacaoTrabalho = criteria.from(EstacaoTrabalho.class);

        criteria.select(estacaoTrabalho);
        if (pesq.getCodigo() != null) {
            criteria.where(builder.equal(estacaoTrabalho.get("codigo"), pesq.getCodigo()));
        }
        if (pesq.getTipo() != null) {
            criteria.where(builder.equal(estacaoTrabalho.get("tipo"), pesq.getTipo()));
        }
        if (pesq.getAtivo() != null) {
            criteria.where(builder.equal(estacaoTrabalho.get("ativo"), pesq.getAtivo()));
        }

        TypedQuery<EstacaoTrabalho> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

}
