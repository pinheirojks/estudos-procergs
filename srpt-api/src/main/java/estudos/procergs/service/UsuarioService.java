package estudos.procergs.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoAutorizadoException;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class UsuarioService {

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
        this.exigirLogin(usuario);
        this.exigirSenha(usuario);
        this.exigirPerfil(usuario);
        this.proibirDuplicacao(usuario);

        usuario.setAtivo(true);
        usuarioRepository.persist(usuario);
        return usuario;
    }

    @Transactional
    public Usuario alterar(Long id, Usuario u) {
        this.verificarPermicoes();
        this.exigirLogin(u);
        this.exigirSenha(u);
        this.exigirPerfil(u);
        this.exigirAtivo(u);
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
        Usuario usuario = usuarioRepository.findById(id);
        usuario.delete();
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

    private void exigirPerfil(Usuario usuario) {
        if (usuario.getPerfil() == null) {
            throw new WebApplicationException("Informe o perfil.");
        }
    }

    private void exigirAtivo(Usuario usuario) {
        if (usuario.getAtivo() == null) {
            throw new WebApplicationException("Informe se está ativo.");
        }
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
