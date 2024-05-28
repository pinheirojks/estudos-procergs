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
public class UsuarioServiceAlteracaoTest extends UsuarioServiceTest {
    
    private void inicializar(){
        excessaoLancada = null;
        usuarioRetornado = null;
        usuariosCadastrados = new ArrayList<>();
        usuariosCadastrados.add(this.criarUsuario(1L));
        usuariosCadastrados.add(this.criarUsuario(2L));
    }

    @Test
    @Order(1)
    @DisplayName("Deve alterar com sucesso")
    public void deveAlterarComSucesso() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L); 

        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        Assertions.assertNull(excessaoLancada, "Nao deve haver erro");
        Assertions.assertNotNull(usuarioRetornado, "Deve retornar um usuario nao nulo");
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve alterar com duplicacao")
    public void naoDeveAlterarComDuplicacao() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setLogin("usuario1");

        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroEsperado("Login já cadastrado.");
    }

    @Test
    @Order(3)
    @DisplayName("Nao deve alterar sem login")
    public void naoDeveAlterarSemLogin() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L);
        usuarioInformado.setLogin(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroEsperado("Informe o login.");
    }

    @Test
    @Order(4)
    @DisplayName("Nao deve alterar sem senha")
    public void naoDeveAlterarSemSenha() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setSenha(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroEsperado("Informe a senha.");
    }

    @Test
    @Order(5)
    @DisplayName("Nao deve alterar sem perfil")
    public void naoDeveAlterarSemPerfil() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao());

        usuarioInformado = this.criarUsuario(2L); 
        usuarioInformado.setPerfil(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroEsperado("Informe o perfil.");
    }

    private void mocarConsulta(Long id) {
        Mockito.when(usuarioRepositoryMock.findById(Mockito.any())).thenReturn(this.criarUsuario(id));
    }

    private void mocarUsuariosDuplicados() {
        List<Usuario> usuariosDuplicados = usuariosCadastrados.stream()
            .filter(u -> u.getLogin().equalsIgnoreCase(usuarioInformado.getLogin()))
            .toList();

        Mockito.when(usuarioRepositoryMock.listarDuplicados(Mockito.any())).thenReturn(usuariosDuplicados);
    }

    private void tentarAlterar() {
        try {
            usuarioRetornado = usuarioService.alterar(usuarioInformado.getId(), usuarioInformado);
        } catch (WebApplicationException e) {
            excessaoLancada = e;
        }
    }
}
