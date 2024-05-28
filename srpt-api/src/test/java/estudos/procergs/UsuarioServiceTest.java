package estudos.procergs;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.repository.UsuarioRepository;
import estudos.procergs.service.UsuarioService;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

public abstract class UsuarioServiceTest {

    @Inject
    protected UsuarioService usuarioService;

    @InjectMock
    protected AutorizacaoRepository autorizacaoRepositoryMock;

    @InjectMock
    protected UsuarioRepository usuarioRepositoryMock;

    protected Usuario usuarioInformado;

    protected Usuario usuarioRetornado;

    protected WebApplicationException erroRegraNegocio;

    protected NaoPermitidoException erroPermissao;

    protected List<Usuario> usuariosCadastrados;

    protected Usuario criarUsuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setLogin("usuario" + id.toString());
        usuario.setSenha("usuario" + id.toString());
        usuario.setPerfil(PerfilUsuarioEnum.ADMINISTRADOR);
        usuario.setAtivo(true);
        return usuario;
    } 

    protected AutorizacaoDTO criarAutorizacao(PerfilUsuarioEnum perfil) {
        AutorizacaoDTO autorizacao = new AutorizacaoDTO();
        autorizacao.setUsuario(criarUsuario(1L));
        autorizacao.getUsuario().setPerfil(perfil);
        autorizacao.setIp("127.0.0.1");
        return autorizacao;
    }

    protected void mocarConsulta(Long id) {
        if (Objects.nonNull(id)) {
            Mockito.when(usuarioRepositoryMock.findById(Mockito.any())).thenReturn(this.criarUsuario(id));
        }
    }

    protected void mocarUsuariosDuplicados() {
        List<Usuario> usuariosDuplicados = usuariosCadastrados.stream()
            .filter(u -> u.getLogin().equalsIgnoreCase(usuarioInformado.getLogin()))
            .toList();

        Mockito.when(usuarioRepositoryMock.listarDuplicados(Mockito.any())).thenReturn(usuariosDuplicados);
    }

    protected void verificarErroPermissao(String mensagemEsperada) {
        Assertions.assertNotNull(erroPermissao, "Deve haver erro");
        Assertions.assertEquals(mensagemEsperada, erroPermissao.getMessage(), "Deve informar a mensagem de erro correta");
        Assertions.assertNull(usuarioRetornado, "Nao deve retornar um usuario");
    }

    protected void verificarErroRegraNegocio(String mensagemEsperada) {
        Assertions.assertNotNull(erroRegraNegocio, "Deve haver erro");
        Assertions.assertEquals(mensagemEsperada, erroRegraNegocio.getMessage(), "Deve informar a mensagem de erro correta");
        Assertions.assertNull(usuarioRetornado, "Nao deve retornar um usuario");
    }
}
