package estudos.procergs.infra;

import java.io.IOException;

import estudos.procergs.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@AutorizacaoRest
public class AutorizacaoRestInterceptor implements ContainerRequestFilter {

  @Inject
  private UsuarioService usuarioService;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String login = requestContext.getHeaderString("login");
    String senha = requestContext.getHeaderString("senha");
    usuarioService.verificarLogin(login, senha);
  }
}