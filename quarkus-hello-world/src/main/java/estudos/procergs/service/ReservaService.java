package estudos.procergs.service;

import java.time.LocalDate;
import java.util.List;

import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.repository.ReservaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class ReservaService {

    @Inject
    private ReservaRepository repository;

    public List<Reserva> listar(ReservaPesq pesq) {
        return repository.listar(pesq);
    }

    public Reserva consultar(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Reserva incluir(Reserva reserva) {
        this.exigirUsuario(reserva);
        this.exigirEstacaoTrabalho(reserva);
        this.exigirData(reserva);
        this.exigirTipo(reserva);
        this.validarData(reserva);

        reserva.setCancelada(false);
        repository.persist(reserva);
        return reserva;
    }

    @Transactional
    public Reserva alterar(Long id, Reserva r) {
        this.exigirUsuario(r);
        this.exigirEstacaoTrabalho(r);
        this.exigirData(r);
        this.exigirTipo(r);
        this.validarData(r);

        Reserva reserva = repository.findById(id);

        reserva.setUsuario(r.getUsuario());
        reserva.setData(r.getData());
        reserva.setEstacaoTrabalho(r.getEstacaoTrabalho());
        reserva.setTipo(r.getTipo());

        return reserva;
    }

    @Transactional
    public void excluir(Long id) {
        Reserva reserva = repository.findById(id);
        repository.delete(reserva);
    }

    @Transactional
    public void cancelar(Long id){
        Reserva reserva = repository.findById(id);
        reserva.setCancelada(true);
    }

    private void exigirUsuario(Reserva reserva) {
        if (reserva.getUsuario() == null) {
            throw new WebApplicationException("Informe o usuário.");
        }
    }

    private void exigirEstacaoTrabalho(Reserva reserva) {
        if (reserva.getEstacaoTrabalho() == null) {
            throw new WebApplicationException("Informe a estação de trabalho.");
        }
    }

    private void exigirData(Reserva reserva) {
        if (reserva.getData() == null) {
            throw new WebApplicationException("Informe a data.");
        }
    }

    private void exigirTipo(Reserva reserva) {
        if (reserva.getTipo() == null) {
            throw new WebApplicationException("Informe o tipo de reserva.");
        }
    }

    private void validarData(Reserva reserva) {
        if (!reserva.getData().isAfter(LocalDate.now())) {
            throw new WebApplicationException("A data não pode ser no passado.");
        }
    }
}
