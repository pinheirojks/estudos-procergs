package estudos.procergs.service;

import java.util.List;

import estudos.procergs.entity.Usuario;
import estudos.procergs.infra.excecao.NaoAutorizadoException;
import estudos.procergs.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listar(Usuario pesq) {
        return usuarioRepository.listar(pesq);
    }

    public Usuario consultar(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario consultar(String login, String senha) {
        return usuarioRepository.consultar(login, senha);
    }

    @Transactional
    public Usuario incluir(Usuario usuario) {
        this.exigirLogin(usuario);
        this.exigirSenha(usuario);
        this.proibirDuplicacao(usuario);

        usuario.setAtivo(true);
        usuario.persist();
        return usuario;
    }

    @Transactional
    public Usuario alterar(Long id, Usuario u) {
        this.exigirLogin(u);
        this.exigirSenha(u);
        this.exigirAtivo(u);
        this.proibirDuplicacao(u);

        Usuario usuario = usuarioRepository.findById(id);

        usuario.setLogin(u.getLogin());
        usuario.setSenha(u.getSenha());
        usuario.setAtivo(u.getAtivo());
        return usuario;
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        usuario.delete();
    }

    private void exigirLogin(Usuario usuario) {
        if (usuario.getLogin() == null) {
            throw new WebApplicationException("Informe o login.");
        }
    }

    private void exigirSenha(Usuario usuario) {
        if (usuario.getSenha() == null) {
            throw new WebApplicationException("Informe a senha.");
        }
    }

    private void exigirAtivo(Usuario usuario) {
        if (usuario.getAtivo() == null) {
            throw new WebApplicationException("Informe se está ativo.");
        }
    }

    private void proibirDuplicacao(Usuario usuario) {
        Usuario pesq = new Usuario();
        pesq.setAtivo(true);
        pesq.setLogin(usuario.getLogin());
        this.listar(pesq).stream()
            .filter(u -> !u.getId().equals(usuario.getId()))  //Para não considerar a proria entidade numa alteracao
            .findAny()
            .ifPresent(u -> {
                throw new WebApplicationException("Login já cadastrado.");
            });
    }

    public void verificarLogin(String login, String senha) {
        if (this.consultar(login, senha) == null) {
            throw new NaoAutorizadoException("Usuário e senha não encontrados.");
        }
    }
}
