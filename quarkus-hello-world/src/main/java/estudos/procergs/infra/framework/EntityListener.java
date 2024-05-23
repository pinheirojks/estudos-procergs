package estudos.procergs.infra.framework;

import java.time.LocalDateTime;

import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@RequestScoped
public class EntityListener {

    @Inject
    private AutorizacaoRepository autorizacaoRepository;

    @PrePersist
    public void prePersist(AbstractEntity entidade) {
        AutorizacaoDTO autorizacao = autorizacaoRepository.getAutorizacao();
        entidade.setDataHoraInclusao(LocalDateTime.now());
        entidade.setIdUsuarioInclusao(autorizacao.getIdUsuario());
        entidade.setIpInclusao(autorizacao.getIp());
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entidade) {
        AutorizacaoDTO autorizacao = autorizacaoRepository.getAutorizacao();
        entidade.setDataHoraAlteracao(LocalDateTime.now());
        entidade.setIdUsuarioAlteracao(autorizacao.getIdUsuario());
        entidade.setIpAlteracao(autorizacao.getIp());
    }
}
