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
    public void prePersist(AbstractEntity entity) {
        AutorizacaoDTO autorizacao = autorizacaoRepository.getAutorizacao();
        entity.setDataHoraInclusao(LocalDateTime.now());
        entity.setIdUsuarioInclusao(autorizacao.getIdUsuario());
        entity.setIpInclusao(autorizacao.getIp());
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        AutorizacaoDTO autorizacao = autorizacaoRepository.getAutorizacao();
        entity.setDataHoraAlteracao(LocalDateTime.now());
        entity.setIdUsuarioAlteracao(autorizacao.getIdUsuario());
        entity.setIpAlteracao(autorizacao.getIp());
    }
}
