package estudos.procergs;

import java.util.ArrayList;
import java.util.List;

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

    private Usuario usuarioInformado;

    private Usuario usuarioRetornado;

    private WebApplicationException excessaoLancada;

    private List<Usuario> usuariosCadastrados;

    private void inicializar(){
        excessaoLancada = null;
        usuarioRetornado = null;
        usuariosCadastrados = new ArrayList<>();
        usuariosCadastrados.add(this.criarUsuario(1L));
    }

    @Test
    @Order(1)
    @DisplayName("Deve incluir com sucesso")
    public void deveIncluirComSucesso() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L); 
        usuarioInformado.setId(null);
        usuarioInformado.setAtivo(null);

        List<Usuario> usuariosDuplicados = usuariosCadastrados.stream()
            .filter(u -> u.getLogin().equalsIgnoreCase(usuarioInformado.getLogin()))
            .toList();

        Mockito.when(usuarioRepositoryMock.listarDuplicados(Mockito.any())).thenReturn(usuariosDuplicados);
        this.tentarIncluir();
        Assertions.assertNull(excessaoLancada, "Nao deve haver erro");
        Assertions.assertNotNull(usuarioRetornado, "Deve retornar um usuario nao nulo");
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve incluir com duplicacao")
    public void naoDeveIncluirComDuplicacao() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(1L);  
        usuarioInformado.setId(null);

        List<Usuario> usuariosDuplicados = usuariosCadastrados.stream()
            .filter(u -> u.getLogin().equalsIgnoreCase(usuarioInformado.getLogin()))
            .toList();

        Mockito.when(usuarioRepositoryMock.listarDuplicados(Mockito.any())).thenReturn(usuariosDuplicados);
        this.tentarIncluir();
        this.verificarExcessaoEsperada("Login já cadastrado.");
    }

    @Test
    @Order(3)
    @DisplayName("Nao deve incluir sem login")
    public void naoDeveIncluirSemLogin() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setLogin(null);
        
        this.tentarIncluir();
        this.verificarExcessaoEsperada("Informe o login.");
    }

    @Test
    @Order(4)
    @DisplayName("Nao deve incluir sem senha")
    public void naoDeveIncluirSemSenha() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setSenha(null);
        
        this.tentarIncluir();
        this.verificarExcessaoEsperada("Informe a senha.");
    }

    @Test
    @Order(5)
    @DisplayName("Nao deve incluir sem perfil")
    public void naoDeveIncluirSemPerfil() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setPerfil(null);
        
        this.tentarIncluir();
        this.verificarExcessaoEsperada("Informe o perfil.");
    }

    private void tentarIncluir() {
        try {
            usuarioRetornado = usuarioService.incluir(usuarioInformado);
        } catch (WebApplicationException e) {
            excessaoLancada = e;
        }
    }

    private void verificarExcessaoEsperada(String mensagemEsperada) {
        Assertions.assertNotNull(excessaoLancada, "Deve haver erro");
        Assertions.assertEquals(mensagemEsperada, excessaoLancada.getMessage(), "Deve informar a mensagem de erro correta");
        Assertions.assertNull(usuarioRetornado, "Nao deve retornar um usuario");
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
