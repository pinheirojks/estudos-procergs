package estudos.procergs;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import estudos.procergs.entity.Usuario;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
public class UsuarioServiceInclusaoTest extends UsuarioServiceTest {
    
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

        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        Assertions.assertNull(excessaoLancada, "Nao deve haver erro");
        Assertions.assertNotNull(usuarioRetornado, "Deve retornar um usuario nao nulo");
    }

    private void mocarUsuariosDuplicados() {
        List<Usuario> usuariosDuplicados = usuariosCadastrados.stream()
            .filter(u -> u.getLogin().equalsIgnoreCase(usuarioInformado.getLogin()))
            .toList();

        Mockito.when(usuarioRepositoryMock.listarDuplicados(Mockito.any())).thenReturn(usuariosDuplicados);
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve incluir com duplicacao")
    public void naoDeveIncluirComDuplicacao() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(1L);  
        usuarioInformado.setId(null);

        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroEsperado("Login já cadastrado.");
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
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroEsperado("Informe o login.");
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
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroEsperado("Informe a senha.");
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
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroEsperado("Informe o perfil.");
    }

    private void tentarIncluir() {
        try {
            usuarioRetornado = usuarioService.incluir(usuarioInformado);
        } catch (WebApplicationException e) {
            excessaoLancada = e;
        }
    }
}
