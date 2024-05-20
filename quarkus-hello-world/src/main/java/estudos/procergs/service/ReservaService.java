package estudos.procergs.service;

import java.time.LocalDate;
import java.util.List;

import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.entity.Reserva;
import estudos.procergs.entity.ReservaPesq;
import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.TipoReservaEnum;
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

        this.complementar(reserva);

        this.validarData(reserva);
        this.validarEstacao(reserva);
        this.validarUsuario(reserva);
        this.proibirReagendamento(reserva);

        reserva.setCancelada(false);
        repository.persist(reserva);

        reserva = repository.findById(reserva.getId());

        return reserva;
    }

    private void complementar(Reserva reserva) {
        reserva.setUsuario(Usuario.findById(reserva.getUsuario().id));
        reserva.setEstacaoTrabalho(EstacaoTrabalho.findById(reserva.getEstacaoTrabalho().id));
    }

    @Transactional
    public Reserva alterar(Long id, Reserva r) {
        this.exigirUsuario(r);
        this.exigirEstacaoTrabalho(r);
        this.exigirData(r);
        this.exigirTipo(r);

        this.complementar(r);

        this.validarData(r);
        this.validarEstacao(r);
        this.validarUsuario(r);
        this.proibirReagendamento(r);

        Reserva reserva = repository.findById(id);

        reserva.setUsuario(r.getUsuario());
        reserva.setData(r.getData());
        reserva.setEstacaoTrabalho(r.getEstacaoTrabalho());
        reserva.setTipo(r.getTipo());

        return reserva;
    }

    private void proibirReagendamento(Reserva r) {
        List<Reserva> reservasAtivasDaData = this.listarReservasAtivasDaData(r.getData());
        this.proibirReagendamentoEstacao(r, reservasAtivasDaData);
        this.proibirReagendamentoUsuario(r, reservasAtivasDaData);
    }

    private List<Reserva> listarReservasAtivasDaData(LocalDate data) {
        return repository.list("data", data).stream()
        .filter(re -> !re.getCancelada()) 
        .toList() ;
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
        if (reserva.getData().isBefore(LocalDate.now())) {
            throw new WebApplicationException("A data não pode ser no passado.");
        }
    }

    private void validarUsuario(Reserva reserva) {
        if (!reserva.getUsuario().getAtivo()) {
            throw new WebApplicationException("O usuário está desativado.");
        } 
    }

    private void validarEstacao(Reserva reserva) {
        if (!reserva.getEstacaoTrabalho().getAtivo()) {
            throw new WebApplicationException("A estação de trabalho está desativada.");
        } 
    }

    private void proibirReagendamentoEstacao(Reserva reserva, List<Reserva> reservasAtivasDaData) {
        reservasAtivasDaData.stream()     
            .filter(r -> r.getEstacaoTrabalho().id.equals(reserva.getEstacaoTrabalho().id))
            .filter(r -> TipoReservaEnum.INTEGRAL.equals(r.getTipo()) || r.getTipo().equals(reserva.getTipo()))
            .filter(r -> !r.getId().equals(reserva.getId()))  //Para não consuderar a proria entidade numa alteracao
            .findAny()
            .ifPresent(r -> {
                throw new WebApplicationException("Já existe um agendamento para esta estação no periodo solicitado.");
            });
    }

    private void proibirReagendamentoUsuario(Reserva reserva, List<Reserva> reservasAtivasDaData) {
        reservasAtivasDaData.stream()           
            .filter(r -> r.getUsuario().id.equals(reserva.getUsuario().id))
            .filter(r -> TipoReservaEnum.INTEGRAL.equals(r.getTipo()) || r.getTipo().equals(reserva.getTipo()))
            .filter(r -> !r.getId().equals(reserva.getId()))  //Para não consuderar a proria entidade numa alteracao
            .findAny()
            .ifPresent(r -> {
                throw new WebApplicationException("Já existe um agendamento deste usuário no periodo solicitado.");
            });
    }
}
