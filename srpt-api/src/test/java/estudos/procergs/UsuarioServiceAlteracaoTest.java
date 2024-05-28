package estudos.procergs;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.infra.excecao.NaoPermitidoException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
public class UsuarioServiceAlteracaoTest extends UsuarioServiceTest {
    
    private void inicializar(){
        erroRegraNegocio = null;
        erroPermissao = null;
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
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L); 

        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        Assertions.assertNull(erroRegraNegocio, "Nao deve haver erro");
        Assertions.assertNotNull(usuarioRetornado, "Deve retornar um usuario nao nulo");
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve alterar com duplicacao")
    public void naoDeveAlterarComDuplicacao() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setLogin("usuario1");

        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroRegraNegocio("Login já cadastrado.");
    }

    @Test
    @Order(3)
    @DisplayName("Nao deve alterar sem login")
    public void naoDeveAlterarSemLogin() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);
        usuarioInformado.setLogin(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroRegraNegocio("Informe o login.");
    }

    @Test
    @Order(4)
    @DisplayName("Nao deve alterar sem senha")
    public void naoDeveAlterarSemSenha() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setSenha(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroRegraNegocio("Informe a senha.");
    }

    @Test
    @Order(5)
    @DisplayName("Nao deve alterar sem perfil")
    public void naoDeveAlterarSemPerfil() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L); 
        usuarioInformado.setPerfil(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroRegraNegocio("Informe o perfil.");
    }

    @Test
    @Order(6)
    @DisplayName("Nao deve alterar com funcionario logado")
    public void naoDeveAlterarComFuncionarioLogado() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.FUNCIONARIO));

        usuarioInformado = this.criarUsuario(1L); 

        this.mocarUsuariosDuplicados();
        this.tentarAlterar();
        this.verificarErroPermissao("Usuário sem permissão para esta operação.");
    }

    private void mocarConsulta(Long id) {
        Mockito.when(usuarioRepositoryMock.findById(Mockito.any())).thenReturn(this.criarUsuario(id));
    }

    private void tentarAlterar() {
        try {
            usuarioRetornado = usuarioService.alterar(usuarioInformado.getId(), usuarioInformado);
        } catch (NaoPermitidoException e) {
            erroPermissao = e;
        } catch (WebApplicationException e) {
            erroRegraNegocio = e;
        }
    }
}
