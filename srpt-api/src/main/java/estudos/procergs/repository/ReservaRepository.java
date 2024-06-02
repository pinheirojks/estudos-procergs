package estudos.procergs.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class ReservaRepository implements PanacheRepository<Reserva> {

    @Inject
    private EntityManager entityManager;

    public List<Reserva> listar(ReservaPesq pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> criteria = builder.createQuery(Reserva.class);
        Root<Reserva> reserva = criteria.from(Reserva.class);

        criteria.select(reserva);
        criteria.where(builder.and(this.montarRestricoes(pesq, builder, reserva)));
        criteria.orderBy(this.montarOrdens(pesq, builder, reserva));

        TypedQuery<Reserva> query = entityManager.createQuery(criteria);
        query = this.montarPaginacao(pesq, query);
        return query.getResultList();
    }

    public Long contar(ReservaPesq pesq) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<Reserva> reserva = criteria.from(Reserva.class);
        criteria.select(builder.count(reserva));
        criteria.where(builder.and(this.montarRestricoes(pesq, builder, reserva)));
        
        return entityManager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] montarRestricoes(ReservaPesq pesq, CriteriaBuilder builder, Root<Reserva> reserva) {
        List<Expression<Boolean>> restricoes = new ArrayList<>();
        Join<Reserva, Usuario> usuario = reserva.join("usuario");
        Join<Reserva, EstacaoTrabalho> estacao = reserva.join("estacaoTrabalho");

        if (Objects.nonNull(pesq.getIdUsuario())) {
            restricoes.add(builder.equal(usuario.get("id"), pesq.getIdUsuario()));
        }
        if (Objects.nonNull(pesq.getIdEstacaoTrabalho())) {
            restricoes.add(builder.equal(estacao.get("id"), pesq.getIdEstacaoTrabalho()));
        }
        if (Objects.nonNull(pesq.getNomeTipo())) {
            restricoes.add(builder.equal(reserva.get("tipo"), pesq.getNomeTipo()));
        }
        if (Objects.nonNull(pesq.getDataInicio())) {
            restricoes.add(builder.greaterThanOrEqualTo(reserva.get("data"), pesq.getDataInicio()));
        }
        if (Objects.nonNull(pesq.getDataFim())) {
            restricoes.add(builder.lessThanOrEqualTo(reserva.get("data"), pesq.getDataFim()));
        }
        if (Objects.nonNull(pesq.getCancelada())) {
            restricoes.add(builder.equal(reserva.get("cancelada"), pesq.getCancelada()));
        }
        return restricoes.stream()
            .toArray(tamanho -> new Predicate[tamanho]);
    }
    
    private List<Order> montarOrdens(ReservaPesq pesq, CriteriaBuilder builder, Root<Reserva> reserva) {
        List<Order> ordens = new ArrayList<>();
        Join<Reserva, Usuario> usuario = reserva.join("usuario");
        Join<Reserva, EstacaoTrabalho> estacao = reserva.join("estacaoTrabalho");

        if (StringUtils.isNotBlank(pesq.getCampoOrdenacao()) && StringUtils.isNotBlank(pesq.getSentidoOrdenacao())) {
            String[] arrayOrdenacao = pesq.getCampoOrdenacao().split("\\.");
            Order ordemParametro;
            Expression<?> campo;

            if (arrayOrdenacao.length > 1 && arrayOrdenacao[0].equals("usuario")) {
                campo = usuario.get(arrayOrdenacao[1]);    
            } else if (arrayOrdenacao.length > 1 && arrayOrdenacao[0].equals("estacaoTrabalho")) {
                campo = estacao.get(arrayOrdenacao[1]);    
            } else {
                campo = reserva.get(pesq.getCampoOrdenacao());
            }

            if (pesq.getSentidoOrdenacao().equalsIgnoreCase("ASC")) {
                ordemParametro = builder.asc(campo);    
            } else {
                ordemParametro = builder.desc(campo);
            }
            ordens.add(ordemParametro);
        }

        ordens.add(builder.asc(reserva.get("data")));
        ordens.add(builder.asc(usuario.get("nome")));
        return ordens;
    }

    private TypedQuery<Reserva> montarPaginacao(ReservaPesq pesq, TypedQuery<Reserva> query) {
        if (Objects.nonNull(pesq.getTamanhoPagina()) && Objects.nonNull(pesq.getNumeroPagina())) {
            query.setMaxResults(pesq.getTamanhoPagina());
            query.setFirstResult((pesq.getNumeroPagina() - 1) * pesq.getTamanhoPagina());
        }
        return query;
    }
}
