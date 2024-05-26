package estudos.procergs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import estudos.procergs.entity.Usuario;
import estudos.procergs.enums.PerfilUsuarioEnum;
import estudos.procergs.service.UsuarioService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
public class UsuarioServiceTest {

    @Inject
    UsuarioService usuarioService;

    @BeforeAll
    public static void setup(){
        UsuarioService mock = Mockito.mock(UsuarioService.class);
        //Mockito.when(mock.verificarPermicoes()).thenReturn(true);
        QuarkusMock.installMockForType(mock, UsuarioService.class);
    }


    @Test
    @DisplayName("Teste de inclusão de usuário sem login")
    @Order(1)
    public void testIncluirUsuarioSemLogin() {
        //QuarkusMock.installMockForInstance(new UsuarioRepository(), usuarioService);
        Usuario usuario = new Usuario();
        usuario.setSenha("123456");
        usuario.setPerfil(PerfilUsuarioEnum.ADMINISTRADOR);
        //Assertions.assertThrows(WebApplicationException.class, () -> {
        //    usuarioService.incluir(usuario);
        //});
        try {
            usuarioService.incluir(usuario);
        } catch (WebApplicationException e) {
            Assertions.assertEquals("Login é obrigatório2", e.getMessage());
        }
    }

}
