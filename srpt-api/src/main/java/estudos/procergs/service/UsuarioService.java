package estudos.procergs.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoAutorizadoException;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.framework.AbstractService;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UsuarioService extends AbstractService {

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private AutorizacaoRepository autorizacaoRepository;

    public List<Usuario> listar(Usuario pesq) {
        return usuarioRepository.listar(pesq);
    }

    public Usuario consultar(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario consultar(String login, String senha) {
        return usuarioRepository.consultar(login, senha);
    }

    public Usuario consultarUsuarioSistema() {
       return consultar(1L);
    }

    @Transactional
    public Usuario incluir(Usuario usuario) {
        this.verificarPermicoes();
        this.exigirString(usuario.getLogin(), "Informe o login.");
        this.exigirString(usuario.getSenha(), "Informe a senha.");
        this.exigir(usuario.getPerfil(), "Informe o perfil.");
        this.proibirDuplicacao(usuario);

        usuario.setAtivo(true);
        usuarioRepository.persist(usuario);
        return usuario;
    }

    @Transactional
    public Usuario alterar(Long id, Usuario u) {
        this.verificarPermicoes();
        this.exigir(u.getId(), "Informe o ID.");
        this.exigirString(u.getLogin(), "Informe o login.");
        this.exigirString(u.getSenha(), "Informe a senha.");
        this.exigir(u.getPerfil(), "Informe o perfil.");
        this.exigir(u.getAtivo(), "Informe se está ativo.");
        this.proibirDuplicacao(u);

        Usuario usuario = usuarioRepository.findById(id);

        usuario.setLogin(u.getLogin());
        usuario.setSenha(u.getSenha());
        usuario.setPerfil(u.getPerfil());
        usuario.setAtivo(u.getAtivo());
        return usuario;
    }

    @Transactional
    public void excluir(Long id) {
        this.verificarPermicoes();
        this.exigir(id, "Informe o ID.");
        Usuario usuario = usuarioRepository.findById(id);
        usuarioRepository.delete(usuario);
    }

    public void verificarLogin(String chave) {
        if (StringUtils.isBlank(chave)) {
            throw new NaoAutorizadoException("Usuário, senha e IP não informados.");
        }
        chave = chave.substring(7); // Remove a string "Bearier " da chave
        String[] loginSenhaIp = chave.split(":");
        String login = loginSenhaIp[0];
        String senha = loginSenhaIp[1];
        Usuario usuario = this.consultar(login, senha);
        if (usuario == null) {
            throw new NaoAutorizadoException("Usuário e senha não encontrados.");
        }  
        if (loginSenhaIp.length < 3) {
            throw new NaoAutorizadoException("IP não informado.");
        }
        String ip = loginSenhaIp[2];
        autorizacaoRepository.incluirAutorizacao(usuario, ip);
    }

    private void proibirDuplicacao(Usuario usuario) {
        usuarioRepository.listarDuplicados(usuario.getLogin()).stream()
            .filter(u -> !u.getId().equals(usuario.getId()))  //Para não considerar a propria entidade numa alteracao
            .findAny()
            .ifPresent(u -> {
                throw new WebApplicationException("Login já cadastrado.");
            });
    }

    private void verificarPermicoes() {
        Usuario usuarioLogado = autorizacaoRepository.getAutorizacao().getUsuario();
        if(!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioLogado.getPerfil())) {
            throw new NaoPermitidoException("Usuário sem permissão para esta operação.");
        }
    }
}
