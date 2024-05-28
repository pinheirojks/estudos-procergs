package estudos.procergs;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
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

    protected WebApplicationException excessaoLancada;

    protected List<Usuario> usuariosCadastrados;

    protected void verificarErroEsperado(String mensagemEsperada) {
        Assertions.assertNotNull(excessaoLancada, "Deve haver erro");
        Assertions.assertEquals(mensagemEsperada, excessaoLancada.getMessage(), "Deve informar a mensagem de erro correta");
        Assertions.assertNull(usuarioRetornado, "Nao deve retornar um usuario");
    }

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
}
