package estudos.procergs.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoAutorizadoException;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.framework.AbstractService;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.integration.cpon.UsuarioCpon;
import estudos.procergs.integration.cpon.UsuarioCponService;
import estudos.procergs.integration.soe.UsuarioSoe;
import estudos.procergs.integration.soe.UsuarioSoeService;
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

    @Inject
    private UsuarioCponService usuarioCponService;   

    @Inject
    private UsuarioSoeService usuarioSoeService;

    @ConfigProperty(name = "autenticacao.cpon.soe.habilitada")
    private String autenticacaoSoeHabilitada;

    public List<Usuario> listar(Usuario pesq) {
        return usuarioRepository.listar(pesq);
    }

    public Usuario consultar(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario consultar(Long matricula, String senha) {
        return usuarioRepository.consultar(matricula, senha);
    }

    public Usuario consultarMatricula(Long matricula) {
        return usuarioRepository.consultar(matricula);
    }

    public Usuario consultarUsuarioSistema() {
        return consultar(1L);
    }

    @Transactional
    public Usuario incluir(Usuario usuario) {
        this.verificarPermicoes();
        this.exigir(usuario.getMatricula(), "Informe a matrícula.");
        this.exigirString(usuario.getNome(), "Informe o nome.");
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
        this.exigir(u.getMatricula(), "Informe a matrícula.");
        this.exigirString(u.getNome(), "Informe o nome.");
        this.exigirString(u.getSenha(), "Informe a senha.");
        this.exigir(u.getPerfil(), "Informe o perfil.");
        this.exigir(u.getAtivo(), "Informe se está ativo.");
        this.proibirDuplicacao(u);

        Usuario usuario = usuarioRepository.findById(id);

        usuario.setMatricula(u.getMatricula());
        usuario.setNome(u.getNome());
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
        String[] matriculaSenhaIp = chave.split(":");
        Long matricula = Long.valueOf(matriculaSenhaIp[0]);
        String senha = matriculaSenhaIp[1];
        if (matriculaSenhaIp.length < 3) {
            throw new NaoAutorizadoException("IP não informado.");
        }
        String ip = matriculaSenhaIp[2];

        if ("S".equalsIgnoreCase(autenticacaoSoeHabilitada)) {
            UsuarioCpon usuarioCpon = usuarioCponService.autenticar(matricula, senha, ip);
            if (usuarioCpon == null) {
                throw new NaoAutorizadoException("Usuário e senha não encontrados no SOE.");
            }
            Usuario usuario = this.consultarMatricula(matricula);
            if (usuario == null) {
                throw new NaoAutorizadoException("Matrícula não cadastrada no sistema.");
            }
            autorizacaoRepository.incluirAutorizacao(usuario, ip);

        } else {
            Usuario usuario = this.consultar(matricula, senha);
            if (usuario == null) {
                throw new NaoAutorizadoException("Matrícula e senha não cadastradas no sistema.");
            }
            autorizacaoRepository.incluirAutorizacao(usuario, ip);
        }
    }

    private void proibirDuplicacao(Usuario usuario) {
        usuarioRepository.listarDuplicados(usuario.getMatricula()).stream()
                .filter(u -> !u.getId().equals(usuario.getId())) // Para não considerar a propria entidade numa
                                                                 // alteracao
                .findAny()
                .ifPresent(u -> {
                    throw new WebApplicationException("Matrícula já cadastrada.");
                });
    }

    private void verificarPermicoes() {
        Usuario usuarioLogado = autorizacaoRepository.getAutorizacao().getUsuario();
        if (!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioLogado.getPerfil())) {
            throw new NaoPermitidoException("Usuário sem permissão para esta operação.");
        }
    }

    @Transactional
    public Long importarNovosUsuariosSOE() {
        Usuario pesq = new Usuario();
        pesq.setAtivo(true);
        List<Usuario> usuariosAtivos = this.listar(pesq);

        List<UsuarioSoe> usuariosSoeNovos = usuarioSoeService.listar("PROCERGS", null, null, "AF1").stream()
            .filter(usuarioSoe -> usuariosAtivos.stream().noneMatch(usuario -> usuario.getMatricula().equals(usuarioSoe.getMatricula())))
            .toList();

        usuariosSoeNovos.stream()
            .map(this::criarUsuario)
            .forEach(usuarioRepository::persist);

        return Long.valueOf(usuariosSoeNovos.size());
    }

    private Usuario criarUsuario(UsuarioSoe usuarioSoe) {
        Usuario usuario = new Usuario();
        usuario.setMatricula(usuarioSoe.getMatricula());
        usuario.setNome(usuarioSoe.getNomeUsuario());
        usuario.setSenha(null);
        usuario.setSiglaSetor(usuarioSoe.getSiglaSetor());
        usuario.setPerfil(PerfilUsuarioEnum.FUNCIONARIO);
        usuario.setAtivo(true);
        return usuario;
    }


}
