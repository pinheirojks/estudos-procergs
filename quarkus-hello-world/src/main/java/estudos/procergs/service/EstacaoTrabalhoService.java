package estudos.procergs.service;

import java.util.List;

import estudos.procergs.entity.EstacaoTrabalho;
import estudos.procergs.repository.EstacaoTrabalhoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class EstacaoTrabalhoService {

    @Inject
    private EstacaoTrabalhoRepository estacaoTrabalhoRepository;

    public List<EstacaoTrabalho> listar(EstacaoTrabalho pesq) {
        return estacaoTrabalhoRepository.listar(pesq);
    }

    public EstacaoTrabalho consultar(Long id) {
        return estacaoTrabalhoRepository.findById(id);
    }

    @Transactional
    public EstacaoTrabalho incluir(EstacaoTrabalho estacao) {
        this.exigirCodigo(estacao);
        this.exigirTipo(estacao);
        this.proibirDuplicacao(estacao);

        estacao.setAtivo(true);
        estacao.persist();
        return estacao;
    }

    @Transactional
    public EstacaoTrabalho alterar(Long id, EstacaoTrabalho e) {
        this.exigirCodigo(e);
        this.exigirTipo(e);
        this.exigirAtivo(e);
        this.proibirDuplicacao(e);

        EstacaoTrabalho estacao = estacaoTrabalhoRepository.findById(id);

        estacao.setCodigo(e.getCodigo());
        estacao.setTipo(e.getTipo());
        estacao.setAtivo(e.getAtivo());
        return estacao;
    }

    @Transactional
    public void excluir(Long id) {
        EstacaoTrabalho estacao = estacaoTrabalhoRepository.findById(id);
        estacao.delete();
    }

    private void exigirCodigo(EstacaoTrabalho estacao) {
        if (estacao.getCodigo() == null) {
            throw new WebApplicationException("Informe o Código.");
        }
    }

    private void exigirTipo(EstacaoTrabalho estacao) {
        if (estacao.getTipo() == null) {
            throw new WebApplicationException("Informe o tipo.");
        }
    }

    private void exigirAtivo(EstacaoTrabalho estacao) {
        if (estacao.getAtivo() == null) {
            throw new WebApplicationException("Informe se está ativo.");
        }
    }

    private void proibirDuplicacao(EstacaoTrabalho estacao) {
        EstacaoTrabalho pesq = new EstacaoTrabalho();
        pesq.setAtivo(true);
        pesq.setCodigo(estacao.getCodigo());
        this.listar(pesq).stream()
                .filter(e -> e.getCodigo().equals(estacao.getCodigo())) //filtro provisório - problema no repository
                .filter(u -> !u.getId().equals(estacao.getId())) // Para não considerar a propria entidade numa alteracao
                .findAny()
                .ifPresent(u -> {
                    throw new WebApplicationException("Estação de trabalho já cadastrada.");
                });
    }

}
