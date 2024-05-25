package estudos.procergs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import estudos.procergs.infra.interceptor.AutorizacaoRepository;
import estudos.procergs.repository.UsuarioRepository;
import estudos.procergs.service.UsuarioService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
public class UsuarioServiceInclusaoTest {

    @Inject
    private UsuarioService usuarioService;

    @InjectMock
    private AutorizacaoRepository autorizacaoRepositoryMock;

    @InjectMock
    private UsuarioRepository usuarioRepositoryMock;

    @Test
    @Order(1)
    @DisplayName("Deve incluir com sucesso")
    public void deveIncluirComSucesso() {
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        Usuario usuario = this.criarUsuario(1L); 
        usuario.setId(null);
        Exception excessao = null;
        Usuario usuarioEsperado = null;
        try {
            usuarioEsperado = usuarioService.incluir(usuario);
        } catch (WebApplicationException e) {
            excessao = e;
        }
        Assertions.assertNull(excessao, "Nao deve haver erro");
        Assertions.assertNotNull(usuarioEsperado, "Deve retornar um usuario");
    }

    @Test
    @Order(1)
    @DisplayName("Nao deve incluir sem login")
    public void naoDeveIncluirSemLogin() {
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        Usuario usuario = this.criarUsuario(1L);  
        usuario.setId(null);
        usuario.setLogin(null);
        Exception excessao = null;
        Usuario usuarioEsperado = null;
        try {
            usuarioEsperado = usuarioService.incluir(usuario);
        } catch (WebApplicationException e) {
            excessao = e;
        }
        Assertions.assertNotNull(excessao, "Deve haver erro");
        Assertions.assertEquals(excessao.getMessage(), "Informe o login.", "Deve informar a mensagem de erro correta");
        Assertions.assertNull(usuarioEsperado, "Nao deve retornar um usuario");
    }

    private Usuario criarUsuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setLogin("usuario" + id.toString());
        usuario.setSenha("usuario" + id.toString());
        usuario.setPerfil(PerfilUsuarioEnum.ADMINISTRADOR);
        usuario.setAtivo(true);
        return usuario;
    }  

    private AutorizacaoDTO criarAutorizacao() {
        AutorizacaoDTO autorizacao = new AutorizacaoDTO();
        autorizacao.setUsuario(criarUsuario(1L));
        autorizacao.setIp("127.0.0.1");
        return autorizacao;
    }
}
