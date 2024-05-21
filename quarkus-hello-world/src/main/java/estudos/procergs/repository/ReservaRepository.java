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
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
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

        criteria.select(reserva);
        this.montarRestricoes(pesq, builder, reserva).stream()
            .forEach(restricao -> criteria.where(restricao));
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

        this.montarRestricoes(pesq, builder, reserva).stream()
            .forEach(restricao -> criteria.where(restricao));        
        return entityManager.createQuery(criteria).getSingleResult();
    }

    private List<Expression<Boolean>> montarRestricoes(ReservaPesq pesq, CriteriaBuilder builder, Root<Reserva> reserva) {
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
            restricoes.add(builder.greaterThanOrEqualTo(reserva.get("dataInicio"), pesq.getDataInicio()));
        }
        if (Objects.nonNull(pesq.getDataFim())) {
            restricoes.add(builder.lessThanOrEqualTo(reserva.get("dataFim"), pesq.getDataFim()));
        }
        if (Objects.nonNull(pesq.getCancelada())) {
            restricoes.add(builder.equal(reserva.get("cancelada"), pesq.getCancelada()));
        }
        return restricoes;
    }

    private List<Order> montarOrdens(ReservaPesq pesq, CriteriaBuilder builder, Root<Reserva> reserva) {
        List<Order> ordens = new ArrayList<>();
        Join<Reserva, Usuario> usuario = reserva.join("usuario");

        if (pesq.getSentidoOrdenacao().equalsIgnoreCase("ASC")) {
            ordens.add(builder.asc(reserva.get(pesq.getCampoOrdenacao())));
        } else {
            ordens.add(builder.desc(reserva.get(pesq.getCampoOrdenacao())));
        }
        ordens.add(builder.asc(reserva.get("data")));
        ordens.add(builder.asc(usuario.get("login")));
        return ordens;
    }

    private TypedQuery<Reserva> montarPaginacao(ReservaPesq pesq, TypedQuery<Reserva> query) {
        if (pesq.getTamanhoPagina().compareTo(Integer.valueOf(0)) > 0 && pesq.getNumeroPagina().compareTo(Integer.valueOf(0)) > 0) {
            query.setMaxResults(pesq.getTamanhoPagina());
            query.setFirstResult((pesq.getNumeroPagina() - 1) * pesq.getTamanhoPagina());
        }
        return query;
    }
}
