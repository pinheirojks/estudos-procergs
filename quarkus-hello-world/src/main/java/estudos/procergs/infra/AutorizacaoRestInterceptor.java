package estudos.procergs.infra;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import estudos.procergs.infra.excecao.NaoAutorizadoException;
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
    requestContext.getHeaders();
    requestContext.getRequest();
    requestContext.getSecurityContext();
    String apiKey = requestContext.getHeaderString("Authorization");

    if (StringUtils.isBlank(apiKey)) {
      throw new NaoAutorizadoException("Usuário e senha não informados.");
    }
    apiKey = apiKey.substring(7);
    String[] loginSenha = apiKey.split(":");
    String login = loginSenha[0];
    String senha = loginSenha[1];
    usuarioService.verificarLogin(login, senha);
  }
}