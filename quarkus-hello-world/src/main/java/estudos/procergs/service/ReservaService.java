package estudos.procergs.service;

import java.util.List;

import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.repository.ReservaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReservaService {

    @Inject
    private ReservaRepository repository;

    public List<Reserva> listar(ReservaPesq pesq) {
        return repository.listar(pesq);
    }
}
