package estudos.procergs.repository;

import java.util.ArrayList;
import java.util.List;

import estudos.procergs.entity.EstacaoTrabalho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

public class EstacaoTrabalhoRepository implements PanacheRepository<EstacaoTrabalho> {

    @Inject
    private EntityManager entityManager;

    public List<EstacaoTrabalho> listar(EstacaoTrabalho pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EstacaoTrabalho> criteria = builder.createQuery(EstacaoTrabalho.class);
        Root<EstacaoTrabalho> estacaoTrabalho = criteria.from(EstacaoTrabalho.class);

        criteria.select(estacaoTrabalho);
        this.montarRestricoes(pesq, builder, estacaoTrabalho).stream()
                .forEach(restricao -> criteria.where(restricao));

        TypedQuery<EstacaoTrabalho> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    public Long contar(EstacaoTrabalho pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<EstacaoTrabalho> estacaoTrabalho = criteria.from(EstacaoTrabalho.class);
        criteria.select(builder.count(estacaoTrabalho));

        this.montarRestricoes(pesq, builder, estacaoTrabalho).stream()
                .forEach(restricao -> criteria.where(restricao));
        return entityManager.createQuery(criteria).getSingleResult();
    }

    private List<Expression<Boolean>> montarRestricoes(EstacaoTrabalho pesq, CriteriaBuilder builder,
            Root<EstacaoTrabalho> estacaoTrabalho) {
        List<Expression<Boolean>> restricoes = new ArrayList<>();
        return restricoes;
    }

}
