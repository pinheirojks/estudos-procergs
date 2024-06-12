package estudos.procergs.service;

import java.util.List;
import java.util.Objects;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import estudos.procergs.dto.AutenticacaoDTO;
import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoAutorizadoException;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.framework.AbstractService;
import estudos.procergs.infra.interceptor.AutorizacaoDTO;
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
    private TokenService tokenService;

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
                .filter(usuarioSoe -> usuariosAtivos.stream()
                        .noneMatch(usuario -> usuario.getMatricula().equals(usuarioSoe.getMatricula())))
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

    public AutorizacaoDTO autenticar(AutenticacaoDTO dto) {
        this.exigir(dto.getMatricula(), "Informe a matrícula.");
        this.exigirString(dto.getSenha(), "Informe a senha.");
        this.exigirString(dto.getIp(), "Informe o IP.");

        if ("S".equalsIgnoreCase(autenticacaoSoeHabilitada)) {
            UsuarioCpon usuarioCpon = usuarioCponService.autenticar(dto.getMatricula(), dto.getSenha(), dto.getIp());
            if (Objects.isNull(usuarioCpon)) {
                throw new NaoAutorizadoException("Usuário e senha não encontrados no SOE.");
            }
        }
        Usuario usuario = this.consultarMatricula(dto.getMatricula());
        if (Objects.isNull(usuario)) {
            throw new NaoAutorizadoException("Matrícula não cadastrada no sistema.");
        }
        AutorizacaoDTO autorizacao = tokenService.adicionar(usuario, dto.getIp());
        return autorizacao;
    }

    public void autorizar(String token) {
        this.exigirString(token, "Informe o token.");
        token = token.substring(7); // Remove a string "Bearier " do Token

        AutorizacaoDTO autorizacao = tokenService.consultar(token);
        if (Objects.isNull(autorizacao)) {
            throw new NaoAutorizadoException("Token não encontrado no sistema.");
        }
        autorizacaoRepository.incluirAutorizacao(autorizacao);
    }
}
