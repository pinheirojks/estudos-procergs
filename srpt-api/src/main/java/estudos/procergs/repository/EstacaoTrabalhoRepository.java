package estudos.procergs.repository;

import java.util.ArrayList;
import java.util.List;

import estudos.procergs.entity.EstacaoTrabalho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
        criteria.where(builder.and(this.montarRestricoes(pesq, builder, estacaoTrabalho)));
        criteria.orderBy(builder.asc(estacaoTrabalho.get("codigo")));
            
        return entityManager.createQuery(criteria).getResultList();
    }

    private Predicate[] montarRestricoes(EstacaoTrabalho pesq, CriteriaBuilder builder, Root<EstacaoTrabalho> estacaoTrabalho) {
        List<Predicate> restricoes = new ArrayList<>();
        if (pesq.getCodigo() != null) {
            restricoes.add(builder.equal(estacaoTrabalho.get("codigo"), pesq.getCodigo()));
        }
        if (pesq.getTipo() != null) {
            restricoes.add(builder.equal(estacaoTrabalho.get("tipo"), pesq.getTipo()));
        }
        if (pesq.getAtivo() != null) {
            restricoes.add(builder.equal(estacaoTrabalho.get("ativo"), pesq.getAtivo()));
        }
        return restricoes.stream()
            .toArray(tamanho -> new Predicate[tamanho]);
    }

}
