package estudos.procergs.service;

import java.util.List;

import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.framework.AbstractService;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.repository.EstacaoTrabalhoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class EstacaoTrabalhoService extends AbstractService {

    @Inject
    private EstacaoTrabalhoRepository repository;

    @Inject
    private AutorizacaoRepository autorizacaoRepository;

    public List<EstacaoTrabalho> listar(EstacaoTrabalho pesq) {
        return repository.listar(pesq);
    }

    public EstacaoTrabalho consultar(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public EstacaoTrabalho incluir(EstacaoTrabalho estacao) {
        this.verificarPermicoes();
        this.exigirString(estacao.getCodigo(), "Informe o código.");
        this.exigir(estacao.getTipo(), "Informe o tipo.");
        this.proibirDuplicacao(estacao);

        estacao.setAtivo(true);
        repository.persist(estacao);
        return estacao;
    }

    @Transactional
    public EstacaoTrabalho alterar(Long id, EstacaoTrabalho e) {
        this.verificarPermicoes();    
        this.exigir(id, "Informe o ID.");    
        this.exigirString(e.getCodigo(), "Informe o código.");
        this.exigir(e.getTipo(), "Informe o tipo.");
        this.exigir(e.getAtivo(), "Informe se está ativo.");
        this.proibirDuplicacao(e);

        EstacaoTrabalho estacao = repository.findById(id);

        estacao.setCodigo(e.getCodigo());
        estacao.setTipo(e.getTipo());
        estacao.setAtivo(e.getAtivo());
        return estacao;
    }

    @Transactional
    public void excluir(Long id) {
        this.verificarPermicoes();    
        this.exigir(id, "Informe o ID."); 
        EstacaoTrabalho estacao = repository.findById(id);
        estacao.delete();
    }

    private void proibirDuplicacao(EstacaoTrabalho estacao) {
        EstacaoTrabalho pesq = new EstacaoTrabalho();
        pesq.setAtivo(true);
        pesq.setCodigo(estacao.getCodigo());
        this.listar(pesq).stream()
                .filter(u -> !u.getId().equals(estacao.getId())) // Para não considerar a propria entidade numa alteracao
                .findAny()
                .ifPresent(u -> {
                    throw new WebApplicationException("Estação de trabalho já cadastrada.");
                });
    }

    private void verificarPermicoes() {
        Usuario usuarioLogado = autorizacaoRepository.getAutorizacao().getUsuario();
        if(!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioLogado.getPerfil())) {
            throw new NaoPermitidoException("Usuário sem permissão para esta operação.");
        }
    }

}
