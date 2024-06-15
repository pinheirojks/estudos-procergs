package estudos.procergs.infra.framework;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class AbstractRepository<E extends AbstractEntity, P> implements PanacheRepository<E>  {

    @Inject
    private EntityManager entityManager;

    public E consultar(Long id) {
        return this.findById(id);
    }

    public E incluir(E entidade) {
        entidade.persistAndFlush();
        return entidade;
    }

    public E alterar(E entidade) {
        entidade.persistAndFlush();
        return entidade;
    }

    public void excluir(Long id) {
        this.deleteById(id);
    }

    public List<E> listar(P pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteria = builder.createQuery(this.getClasseEntidade());
        Root<E> raiz = criteria.from(this.getClasseEntidade());

        criteria.select(raiz);
        criteria.where(builder.and(this.montarRestricoes(pesq, builder, raiz)));
        criteria.orderBy(this.montarOrdens(pesq, builder, raiz));

        TypedQuery<E> query = entityManager.createQuery(criteria);
        query = this.montarPaginacao(pesq, query);
        return query.getResultList();
    }

    public Long contar(P pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<E> raiz = criteria.from(this.getClasseEntidade());
        criteria.select(builder.count(raiz));
        criteria.where(builder.and(this.montarRestricoes(pesq, builder, raiz)));
        
        return entityManager.createQuery(criteria).getSingleResult();
    }

    protected abstract Class<E> getClasseEntidade();

    protected Predicate[] montarRestricoes(P pesq, CriteriaBuilder builder, Root<E> raiz) {
        return new Predicate[0];
    }
    
    protected List<Order> montarOrdens(P pesq, CriteriaBuilder builder, Root<E> raiz) {
        return new ArrayList<>();
    }

    protected TypedQuery<E> montarPaginacao(P pesq, TypedQuery<E> query) {
        return query;
    }
}
