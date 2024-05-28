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
public class UsuarioServiceExclusaoTest extends UsuarioServiceTest {
    
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
    @DisplayName("Deve excluir com sucesso")
    public void deveExcluirComSucesso() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L); 

        this.mocarConsulta(usuarioInformado.getId());
        this.tentarExcluir();
        Assertions.assertNull(erroRegraNegocio, "Nao deve haver erro");
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve excluir sem ID")
    public void naoDeveExcluirSemId() {
        this.inicializar();
        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.ADMINISTRADOR));

        usuarioInformado = this.criarUsuario(2L);
        usuarioInformado.setId(null);
        
        this.mocarConsulta(usuarioInformado.getId());
        this.tentarExcluir();
        this.verificarErroRegraNegocio("Informe o ID.");
    }

    @Test
    @Order(3)
    @DisplayName("Nao deve excluir com funcionario logado")
    public void naoDeveExcluirComFuncionarioLogado() {
        this.inicializar();

        Mockito.when(autorizacaoRepositoryMock.getAutorizacao()).thenReturn(this.criarAutorizacao(PerfilUsuarioEnum.FUNCIONARIO));

        usuarioInformado = this.criarUsuario(1L); 

        this.mocarConsulta(usuarioInformado.getId());
        this.tentarExcluir();
        this.verificarErroPermissao("Usuário sem permissão para esta operação.");
    }

    private void tentarExcluir() {
        try {
            usuarioService.excluir(usuarioInformado.getId());
        } catch (NaoPermitidoException e) {
            erroPermissao = e;
        } catch (WebApplicationException e) {
            erroRegraNegocio = e;
        }
    }
}
