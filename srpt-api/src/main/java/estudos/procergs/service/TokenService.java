package estudos.procergs.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import estudos.procergs.entity.Usuario;
import estudos.procergs.infra.interceptor.AutorizacaoDTO;
import jakarta.inject.Singleton;

@Singleton
public class TokenService {
    private Map<String, AutorizacaoDTO> tokensEmMemoria = new HashMap<String, AutorizacaoDTO>();
  
  public synchronized AutorizacaoDTO adicionar(Usuario usuario, String ip) {
    String token = UUID.randomUUID().toString();
    AutorizacaoDTO autorizacao = new AutorizacaoDTO();
    autorizacao.setUsuario(usuario);
    autorizacao.setToken(token);
    autorizacao.setIp(ip);
    tokensEmMemoria.put(token, autorizacao);
    return autorizacao;
  }
  
  public synchronized AutorizacaoDTO consultar(String token) {  
    if (Objects.isNull(token)) {
      return null;
    }
    return tokensEmMemoria.get(token);
  }
}
