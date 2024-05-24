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
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UsuarioServiceTest {

    @Inject
    UsuarioService usuarioService;

    @BeforeAll
    public static void setup(){
        UsuarioService mock = Mockito.mock(UsuarioService.class);
        Mockito.when(mock.verificarPermicoes()).thenReturn(true);
    }


    @Test
    @Order(1)
    @DisplayName("Teste da Inclusão de Usuario Não Nulo")
    public void testInclusaoUsuarioNotNull() {
        Usuario usuario = new Usuario();
        usuario.setLogin("testLogin");
        usuario.setSenha("testSenha");
        usuario.setPerfil(PerfilUsuarioEnum.ADMINISTRADOR);
        usuario = usuarioService.incluir(usuario);
        Assertions.assertNotNull(usuario);
    }
    @Test
    @Order(2)
    @DisplayName("Teste da Inclusão de Usuario ID Não Nulo")
    public void testInclusaoUsuarioIdNotNull() {
        Usuario usuario = new Usuario();
        usuario.setLogin("testLogin");
        usuario.setSenha("testSenha");
        usuario.setPerfil(PerfilUsuarioEnum.ADMINISTRADOR);
        usuario = usuarioService.incluir(usuario);
        Assertions.assertNotNull(usuario.getId());
    }

    


}
