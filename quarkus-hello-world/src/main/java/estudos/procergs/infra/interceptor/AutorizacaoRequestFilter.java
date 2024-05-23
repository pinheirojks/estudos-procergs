package estudos.procergs.infra.interceptor;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import estudos.procergs.infra.excecao.NaoAutorizadoException;
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

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String chave = requestContext.getHeaderString("Authorization");

    if (StringUtils.isBlank(chave)) {
      throw new NaoAutorizadoException("Usuário e senha não informados.");
    }
    chave = chave.substring(7); //Remove a string "Bearier " da chave
    String[] loginSenha = chave.split(":");
    usuarioService.verificarLogin(loginSenha[0], loginSenha[1]);
  }
}