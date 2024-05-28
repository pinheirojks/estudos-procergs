package estudos.procergs.infra.interceptor;

import java.io.IOException;

import estudos.procergs.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@AutorizacaoInterceptor
public class AutorizacaoRequestFilter implements ContainerRequestFilter {

  @Inject
  private UsuarioService usuarioService;

  // @Inject
  // private UsuarioSoeService usuarioSoeService;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String chave = requestContext.getHeaderString("Authorization");
    usuarioService.verificarLogin(chave);
    // usuarioSoeService.verificarLogin(chave);
  }
}