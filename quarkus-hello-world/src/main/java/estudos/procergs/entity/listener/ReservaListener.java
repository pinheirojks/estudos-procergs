package estudos.procergs.entity.listener;

import estudos.procergs.entity.Reserva;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

@RequestScoped
public class ReservaListener {

    @Inject
    private AutorizacaoRepository autorizacaoRepository;

    @PrePersist
    public void prePersist(Reserva reserva) {
        verificarPermissoes(reserva, "Usuário não pode incluir reserva para outro usuário.");
    }

    @PreUpdate
    public void preUpdate(Reserva reserva) {
        verificarPermissoes(reserva, "Usuário não pode alterar reserva de outro usuário.");
    }

    @PreRemove
    public void preRemove(Reserva reserva) {
        verificarPermissoes(reserva, "Usuário não pode excluir reserva de outro usuário.");
    }

    private void verificarPermissoes(Reserva reserva, String mensagemErro) {
        AutorizacaoDTO autorizacao = autorizacaoRepository.getAutorizacao();
        if(PerfilUsuarioEnum.ADMINISTRADOR.equals(autorizacao.getUsuario().getPerfil())) {
            return;
        }
        if(reserva.getUsuario().getId() != autorizacao.getUsuario().getId()) {
            throw new NaoPermitidoException(mensagemErro);
        }
    }
}
