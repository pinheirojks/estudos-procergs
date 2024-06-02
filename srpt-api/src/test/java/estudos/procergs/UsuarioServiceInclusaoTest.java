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
public class UsuarioServiceInclusaoTest extends UsuarioServiceTest {

    private void inicializar(){
        erroRegraNegocio = null;
        erroPermissao = null;
        usuarioRetornado = null;
        usuariosCadastrados = new ArrayList<>();
        usuariosCadastrados.add(this.criarUsuario(1L));
    }

    @Test
    @Order(1)
    @DisplayName("Deve incluir com sucesso")
    public void deveIncluirComSucesso() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L); 
        usuarioInformado.setId(null);
        usuarioInformado.setAtivo(null);

        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        Assertions.assertNull(erroRegraNegocio, "Nao deve haver erro");
        Assertions.assertNotNull(usuarioRetornado, "Deve retornar um usuario nao nulo");
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve incluir com matricula duplicada")
    public void naoDeveIncluirComMatriculaDuplicada() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(1L);  
        usuarioInformado.setId(null);

        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroRegraNegocio("Matrícula já cadastrada.");
    }

    @Test
    @Order(3)
    @DisplayName("Nao deve incluir sem matricula")
    public void naoDeveIncluirSemMatricula() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setMatricula(null);
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroRegraNegocio("Informe a matrícula.");
    }

    @Test
    @Order(4)
    @DisplayName("Nao deve incluir sem nome")
    public void naoDeveIncluirSemNome() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setNome(null);
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroRegraNegocio("Informe o nome.");
    }

    @Test
    @Order(5)
    @DisplayName("Nao deve incluir sem senha")
    public void naoDeveIncluirSemSenha() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setSenha(null);
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroRegraNegocio("Informe a senha.");
    }

    @Test
    @Order(6)
    @DisplayName("Nao deve incluir sem perfil")
    public void naoDeveIncluirSemPerfil() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);  
        usuarioInformado.setId(null);
        usuarioInformado.setPerfil(null);
        
        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroRegraNegocio("Informe o perfil.");
    }

    @Test
    @Order(7)
    @DisplayName("Nao deve incluir com funcionario logado")
    public void naoDeveIncluirComFuncionarioLogado() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.FUNCIONARIO));

        usuarioInformado = this.criarUsuario(1L);  
        usuarioInformado.setId(null);

        this.mocarUsuariosDuplicados();
        this.tentarIncluir();
        this.verificarErroPermissao("Usuário sem permissão para esta operação.");
    }

    private void tentarIncluir() {
        try {
            usuarioRetornado = usuarioService.incluir(usuarioInformado);
        } catch (NaoPermitidoException e) {
            erroPermissao = e;
        } catch (WebApplicationException e) {
            erroRegraNegocio = e;
        }
    }
}
